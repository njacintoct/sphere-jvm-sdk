package io.sphere.sdk.categories;

import java.util.Optional;
import io.sphere.sdk.utils.SphereInternalLogger;
import io.sphere.sdk.models.Reference;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;

import static io.sphere.sdk.utils.ListUtils.*;
import static io.sphere.sdk.utils.SphereInternalLogger.getLogger;

final class CategoryTreeFactory {

    private static final SphereInternalLogger LOGGER = getLogger("categories.objects");

    private CategoryTreeFactory() {
    }

    public static CategoryTree create(final List<Category> allCategoriesAsFlatListWithoutChildrenSettings) {
        final List<Category> categoryTreeList = categoriesOrganizedAsTree(allCategoriesAsFlatListWithoutChildrenSettings);
        final List<Category> allAsFlatList = getAllRecursive(categoryTreeList);
        final Map<LocaleSlugPair, Category> categoriesByLocaleAndSlug = buildBySlugMap(allAsFlatList);
        final Map<String, Category> categoriesById = buildByIdMap(allAsFlatList);
        return new CategoryTreeImpl(categoryTreeList, allAsFlatList, categoriesByLocaleAndSlug, categoriesById);
    }

    private static Map<String, Category> buildByIdMap(Collection<Category> categories) {
        final Map<String, Category> map = new HashMap<>(categories.size());
        for (Category c: categories) {
            map.put(c.getId(), c);
        }
        return map;
    }

    private static Map<LocaleSlugPair, Category> buildBySlugMap(final Collection<Category> categories) {
        final Map<LocaleSlugPair, Category> map = new HashMap<>();
        for (final Category category : categories) {
            for (final Locale locale : category.getSlug().getLocales()) {
                map.put(new LocaleSlugPair(locale, category.getSlug().get(locale).get()), category);
            }
        }
        return map;
    }

    private static List<Category> categoriesOrganizedAsTree(List<Category> allCategoriesAsFlatListWithoutChildrenSettings) {
        final List<Category> categoriesOrganizedInTrees;
        if (allCategoriesAsFlatListWithoutChildrenSettings == null) {
            categoriesOrganizedInTrees = Collections.emptyList();
            LOGGER.warn(() -> "null passed for categories.");
        } else if (allCategoriesAsFlatListWithoutChildrenSettings.size() == 0) {
            categoriesOrganizedInTrees = Collections.emptyList();
        } else {
            final Pair<List<Category>, List<Category>> partition = partition(allCategoriesAsFlatListWithoutChildrenSettings, new CategoryHasParentPredicate());
            final List<Category> rootCategories = partition.getLeft();
            final List<Category> categoriesWithoutParents = partition.getRight();
            final Multimap<String, Category> categoriesByParentId = buildParentMultiMap(rootCategories);
            categoriesOrganizedInTrees = buildTreeRecursive(Optional.<Category>empty(), categoriesWithoutParents, new ArrayList<>(), categoriesByParentId);
        }
        return categoriesOrganizedInTrees;
    }

    private static List<Category> getAllRecursive(final Iterable<Category> categories) {
        final List<Category> result = new ArrayList<>();
        for (Category c: categories) {
            result.add(c);
            result.addAll(getAllRecursive(c.getChildren()).stream().collect(Collectors.toList()));
        }
        return result;
    }

    private static Multimap<String, Category> buildParentMultiMap(List<Category> categoriesWithParents) {
        final Multimap<String, Category> categoriesByParentId = new Multimap<>();
        for (final Category categoryWithParent : categoriesWithParents) {
            final String parentId = categoryWithParent.getParent().get().getId();
            categoriesByParentId.put(parentId, categoryWithParent);
        }
        return categoriesByParentId;
    }

    private static List<Category> buildTreeRecursive(final Optional<Category> parent,
                                                     final Collection<Category> backendChildren,
                                                     final List<Category> pathInTree,
                                                     final Multimap<String, Category> categoriesByParent) {
        final List<Category> result = new ArrayList<>();
        for (final Category child : backendChildren) {
            pathInTree.add(child);
            // We need some (private) mutability - it's hard to build truly immutable object graphs with circular references
            // http://stackoverflow.com/questions/7507965/instantiating-immutable-paired-objects
            final List<Category> childrenForCategory = immutableCopyOf(buildTreeRecursive(Optional.of(child), categoriesByParent.get(child.getId()), pathInTree, categoriesByParent));
            final List<Category> pathInTreeForCategory = immutableCopyOf(pathInTree);
            final Optional<Reference<Category>> parentForCategory = parent.map(p -> p.toReference());
            //double mapping needed?
            final Category filledCategory = new CategoryWrapper(child) {
                @Override
                public List<Category> getChildren() {
                    return childrenForCategory;
                }

                @Override
                public List<Category> getPathInTree() {
                    return pathInTreeForCategory;
                }

                @Override
                public Optional<Reference<Category>> getParent() {
                    return parentForCategory;
                }
            };
            pathInTree.remove(pathInTree.size() - 1);    // c.pathInTree ends with c itself
            result.add(filledCategory);
        }
        return result;
    }
}

package io.sphere.sdk.categories;

import com.fasterxml.jackson.core.type.TypeReference;
import io.sphere.sdk.client.HttpMethod;
import io.sphere.sdk.client.HttpRequest;
import io.sphere.sdk.client.PagedQueryResult;
import io.sphere.sdk.client.Query;

import java.util.Locale;

import static io.sphere.sdk.utils.UrlUtils.urlEncode;

/**
 * This is an example of hard coding queries without using helper classes.
 */
public class CategoryByNameQuery implements Query<Category, CategoryImpl> {
    private final Locale locale;
    private final String name;

    public CategoryByNameQuery(final Locale locale, final String name) {
        this.locale = locale;
        this.name = name;
    }

    @Override
    public HttpRequest httpRequest() {
        return HttpRequest.of(HttpMethod.GET, "/categories?where=" + urlEncode("name(" + locale.toLanguageTag() + "=\"" + name + "\")"));
    }

    @Override
    public TypeReference<PagedQueryResult<CategoryImpl>> typeReference() {
        return new TypeReference<PagedQueryResult<CategoryImpl>>() {

        };
    }

}
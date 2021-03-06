package io.sphere.sdk.meta;

import io.sphere.sdk.models.Base;

/**
 <p>The Query API is for reading specific resources from SPHERE.IO.
 The resources can be sorted and fetched in batches.</p>

 <p>First, you need to specify a query, example:</p>

 {@include.example io.sphere.sdk.queries.QueryDemo#formulatingAQuery()}

 <p>Second, you use the sphere client to execute a query request, example:</p>

 {@include.example io.sphere.sdk.queries.QueryDemo#executeQuery()}

 <p>The successful execution of a {@link io.sphere.sdk.queries.Query} results in {@link io.sphere.sdk.queries.PagedQueryResult} of a SPHERE.IO resource or view.
 While the {@link io.sphere.sdk.queries.Query} interface just contains information to execute a query,
 the interface {@link io.sphere.sdk.queries.QueryDsl} provides also a domain specific language to tune a query.</p>

 <p>For most of the SPHERE.IO resources you can find classes to support you formulating valid queries (in a sub package queries).</p>

 <p>The following snipped creates a query which selects all products without a specific order.</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#queryForAllDemo()}

 <h3 id="creating-queries">Creating queries</h3>

  There are several ways to create a query:

  <ol>
      <li><a href="#create-query-with-model-companion-class">Create a query with helper methods from a model companion class</a></li>
      <li><a href="#create-query-with-predicates">Create a query with predicate API</a></li>
      <li><a href="#create-hard-coded-query">Create a hard coded query</a></li>
  </ol>

 <h4 id="create-query-with-model-companion-class">Create a query with helper methods from a model companion class</h4>

 <p>For some model interfaces exist companion classes (the name of the interface in plural) which provide some default queries, example for categories:</p>

 {@include.example io.sphere.sdk.queries.QueryDemo#queryFromCompanionHelper()}


 <h4 id="create-query-with-predicates">Create a query with predicate API</h4>

 <p>For queryable model interface exists a query model class, e.g., for {@code io.sphere.sdk.categories.Category}
 exists {@code io.sphere.sdk.categories.CategoryQueryModel}, which contains a DSL to find and specify
 the queryable parameters.
 </p>

 {@include.example io.sphere.sdk.queries.QueryDemo#categoryQueryModel()}

 <p>The generic code looks verbose, but in the future it enables powerful type-safe queries with IDE discovery even on deep
 nested data structures like products. (coming soon)</p>

 The {@link io.sphere.sdk.queries.QueryDsl} class, used by the query model classes, sorts by default by ID and has no offset
 or limit specified. The following example shows how to specify sorting, limiting and skipping pages.

 {@include.example io.sphere.sdk.queries.QueryDemo#withPagination()}

 {@link io.sphere.sdk.queries.QueryDsl} is an interface for immutable objects, so if you call {@code withXyz(value)}
 it returns a new immutable object:

 {@include.example io.sphere.sdk.queries.QueryDemo#immutableQueryDsl()}

 <p>As a result you can construct the query for the next page by using the previous query:</p>

 {@include.example io.sphere.sdk.queries.QueryDemo#nextPage()}


 <h4 id="create-hard-coded-query">Create a hard coded query</h4>

 {@link io.sphere.sdk.queries.Query} is an interface, so you have the opportunity to code
 a query without the domain specific language:

 {@include.example io.sphere.sdk.categories.CategoryByNameQuery}

 <h3 id=predicates>Predicates</h3>

 <h4>Helper methods for common use cases</h4>

 <p>A common use case to query a product is to query it by slug. For this and other most common use cases exist helper methods as shown in the next example.</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#queryBySlug()}

 You may have noticed that the type of the query is not {@link io.sphere.sdk.products.queries.ProductQuery} anymore but {@link io.sphere.sdk.queries.QueryDsl} which does not contain the method {@link io.sphere.sdk.products.queries.ProductQuery#bySlug(io.sphere.sdk.products.ProductProjectionType, java.util.Locale, String)}.
 That is due to the implementation of the domain specific language but still enables you to configure pagination and sorting.

 <p>Important to know is that the {@link io.sphere.sdk.queries.QueryDsl} uses immutable objects, so the call of {@link io.sphere.sdk.products.queries.ProductQuery#bySlug(io.sphere.sdk.products.ProductProjectionType, java.util.Locale, String)} does not change the internal state of the {@link io.sphere.sdk.products.queries.ProductQuery} but creates a new {@link io.sphere.sdk.queries.QueryDsl} object with the selected predicate.</p>

 <h4>Self constructed predicates for special cases</h4>

<p>For more advanced queries you have to use the {@link io.sphere.sdk.queries.Predicate Predicate API}. For example querying for some names you can use:</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#queryByNames()}

 <p>The Predicate API looks verbose, but it is its goal to prevent typos and make it discoverable via the IDE for which attributes can be queried.</p>

 You can still create predicates from strings, but you need to escape characters.

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#testX()}

 <p>For creating predicates for resource &lt;RESOURCE&gt; there is a method {@code model()} in a class &lt;RESOURCE&gt;Query (e.g., Product has ProductQuery).</p>

 <h4>Connecting predicates</h4>

 <p>For connecting predicates use {@link io.sphere.sdk.queries.Predicate#and(io.sphere.sdk.queries.Predicate)} and {@link io.sphere.sdk.queries.Predicate#or(io.sphere.sdk.queries.Predicate)}.</p>


 <p>The following example shows a query for a product where either the name is "foo" <strong>{@code or}</strong> the id is "bar".</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#predicateOrExample()}

 <p>To query for two conditions use <strong>{@code and}</strong>. The following example queries for the name foo and the membership in a category with id "cat1".</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#predicateAndExample()}

 <p>Since the previous queries have a common path you can formulate a combined query with <strong>{@code where}</strong>, but this is not possible for every entity.</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#predicateAndWithWhereExample()}


 <h4>Negate predicates</h4>

 It is not possible to negate complete predicates, but it is possible on the field level. So for example the next query is for products where the name is not exactly "foo".

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#predicateNotExample()}

 <h3 id=sorting>Sorting</h3>

 For paginated queries and consistent user visualization it makes sense to sort the search results. If you don't specify the sorting, results can appear in a random order.
 <p>Some implementations sort by ID by default so even if you forget to specify any sorting options, the order of the results is constant.</p>

 <p>To specify the sorting use {@link io.sphere.sdk.queries.QueryDsl#withSort(java.util.List)}.</p>
 The following example sorts by name in the English locale.

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#sortByName()}

 You can sort by multiple values, for example for name ascending and ID descending:

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#sortByNameAscAndIdDesc()}

Like in the case of the predicates, you can traverse the query model tree to specify a tested and type-safe sort expression. But instead of providing values uses the {@code sort()} method.

 <p>If the SDK lacks of a method to create the sort expression, you can still provide it via a string:</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#sortCreationByString()}

 <h3 id=pagination>Pagination</h3>

 <p>Assumption: in a product query all products are sorted by ID by default.</p>

 A query might produce more results than you want to consume or SPHERE.IO lets you consume. At the time of
 writing you can only fetch up to 500 objects at once.

 <p>Imagine you have 15 products:</p>

 <pre> 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 </pre>

 <p>And a query:</p>

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#queryAllExampleInPaginationContext()}

 <p>If you do not specify a limitation of how many resources should be fetched with one query
 with {@link io.sphere.sdk.queries.QueryDsl#withLimit(long)}, they all (the 15 products) will be loaded.</p>

 <p>If you specify a limit of 4 to a query, this query will only load the first 4 products:</p>
 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#limitProductQueryTo4()}
<pre>|00 01 02 03|</pre>


 To load the next 4 products you need to define an offset with {@link io.sphere.sdk.queries.QueryDsl#withOffset(long)}.

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#limitProductQueryTo4PlusOffset4()}
 <pre>|04 05 06 07|</pre>

 To fetch the products with the ID 08 to 11 you need an offset parameter of 8.
 <p>If you use an offset of 2 and a limit of 4 you will fetch products 02, 03, 04, 05.</p>

 <pre>  00 01|02 03 04 05|06 07 08 09 10 11 12 13 14
 skip 2|limit of 4 | not fetched</pre>

 <p>The fetched result of a query will be a {@link io.sphere.sdk.queries.PagedQueryResult}.</p>

 <ul>
 <li>{@link io.sphere.sdk.queries.PagedQueryResult#getResults()} contains all fetched elements.</li>
 <li>The number of fetched elements can be obtained with {@link io.sphere.sdk.queries.PagedQueryResult#size()}.</li>
 <li>{@link io.sphere.sdk.queries.PagedQueryResult#getOffset()} corresponds to the offset of the query.</li>
 <li>{@link io.sphere.sdk.queries.PagedQueryResult#getTotal()} is the amount of resources matching the query but do not necessarily need to be included in the {@link io.sphere.sdk.queries.PagedQueryResult}.</li>
 </ul>

 <p>So for this example with offset 2 and limit 4 in the query the {@link io.sphere.sdk.queries.PagedQueryResult} will have offset 2, size 4 and total 15. But be careful, count can be smaller than limit in some cases, for example if total is smaller than the limit (limit 500 but only 15 products). It can also be the case when total is not dividable by limit and the last elements are fetched (.e.g. |12 13 14| with offset 12 and limit 4).</p>

 <h3 id=reference-expansion>Reference expansion</h3>

 Reference expansion is a feature of certain query endpoints that enables clients to request
 server-side expansion of Reference resources, thereby reducing the number of required
 client-server roundtrips to obtain the data that a client needs for a specific use-case.
 A query endpoint that supports reference expansion does so by providing an expand query
 parameter which can be used to specifiy one or more expansion paths.

 <p>For example {@link io.sphere.sdk.products.Product} contains a member
 {@link io.sphere.sdk.products.Product#getProductType()}, which has a reference to a {@link io.sphere.sdk.producttypes.ProductType}.

 By default the field {@link io.sphere.sdk.models.Reference#getObj()} will return an absent optional.
 If you specify an expansion path with {@link io.sphere.sdk.queries.QueryDsl#withExpansionPaths(java.util.List)}
 it is possible that the product type will be filled and you can work with it.
 </p>

 Reference expansion is a feature without any guarantees. So if you provide a malformatted
 reference expansion path it will swallow the problem and just fullfill the query. Even if the
 expansion path is correct, it is possible that it will not be expanded. This can for example
 happen if too many resources at once are expanded.

 <p>To make it harder to construct invalid paths, some resources provide methods to generate valid paths.
 You will find them in &lt;RESOURCE&gt;Query.expansionPath().</p>

 So if you want to expand the product type of a product you can query like this:

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#expandProductTypeForProduct()}

 Expansion paths are not limited to the next element, for example you can expand the
 categories for a product projection and expand the parent category of the category you previously have expanded:

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#expandCategoryAndCategoryParentForProduct()}

 If the SDK does not provide a type-safe way to construct a reference path, you can create it by string:

 {@include.example io.sphere.sdk.meta.QueryDocumentationTest#createExpansionPathByString()}


 */
public class QueryDocumentation extends Base {
    private QueryDocumentation() {
    }
}

package io.sphere.sdk.meta;

/**

 <h3>1.0.0-M9</h3>
 <ul>
    <li>Added {@link io.sphere.sdk.meta.KnownIssues Known Issues} page.</li>
    <li>Added experimental support for uploading product images in variants. See {@link io.sphere.sdk.products.commands.ExperimentalProductImageUploadCommand}.</li>
    <li>Added factory methods for {@link io.sphere.sdk.models.Image}.</li>
    <li>{@link io.sphere.sdk.models.Image} contains directly getters for width {@link io.sphere.sdk.models.Image#getWidth()} 
    and height {@link io.sphere.sdk.models.Image#getHeight()}.</li>
    <li>{@link io.sphere.sdk.queries.PagedQueryResult} is constructable for empty results. Before this, the SDK throwed an Exception.</li>
    <li>Fields called {@code quantity} are now of type long instead of int.</li>
    <li>Added a documentation page {@link io.sphere.sdk.meta.ConstructionDocumentation how to construct objects}.</li>
 </ul>

 <h3>1.0.0-M8</h3>

 <ul>
    <li>Query models contain id, createdAt and lastModifiedAt for predicates and sorting.</li>
    <li>Introduced endpoints and models for {@link io.sphere.sdk.carts.Cart}s, {@link io.sphere.sdk.customers.Customer}s, {@link io.sphere.sdk.customergroups.CustomerGroup}s and {@link io.sphere.sdk.orders.Order}s.</li>
    <li>Quantity fields are now of type long.</li>
    <li>Classes like {@link io.sphere.sdk.products.queries.ProductFetchById} take now a string parameter for the ID and not an {@link io.sphere.sdk.models.Identifiable}.</li>
    <li>Queries, Fetches, Commands and Searches are only instantiable with an static of method like {@link io.sphere.sdk.categories.commands.CategoryCreateCommand#of(io.sphere.sdk.categories.CategoryDraft)}. The instantiation by constructor is not supported anymore.</li>
    <li>Enum constant names are only in upper case.</li>
 </ul>

  <h3>1.0.0-M7</h3>

 <ul>
    <li>Incompatible change: Classes to create templates for new entries in SPHERE.IO like {@code NewCategory} have been renamed to {@link io.sphere.sdk.categories.CategoryDraft}. </li>
    <li>Incompatible change: {@link io.sphere.sdk.producttypes.ProductTypeDraft} has now only
 factory methods with an explicit parameter for the attribute declarations to prevent to use
 the getter {@link io.sphere.sdk.producttypes.ProductTypeDraft#getAttributes()} and list add operations. </li>
    <li>Incompatible change: {@code LocalizedString} has been renamed to {@link io.sphere.sdk.models.LocalizedStrings}, since it is not a container for one string and a locale, but for multiple strings of different locals. It is like a map.</li>
    <li>Incompatible change: The {@link io.sphere.sdk.queries.Fetch} classes have been renamed. From FetchRESOURCEByWhatever to RESOURCEFetchByWhatever</li>
    <li>Moved Scala and Play clients out of the Git repository to <a href="https://github.com/sphereio/sphere-jvm-sdk-scala-add-ons">https://github.com/sphereio/sphere-jvm-sdk-scala-add-ons</a>. The artifact ID changed.</li>
    <li>{@link io.sphere.sdk.meta.SphereResources} contains now also a listing of queries and commands for the resources.</li>
    <li>Added {@link io.sphere.sdk.products.search.ProductProjectionSearch} for full-text, filtered and faceted search.</li>
    <li>Incompatible change: {@link io.sphere.sdk.products.ProductUpdateScope} makes it more visible that product update operations can be for only staged or for current and staged. The product update actions will be affected by that.</li>
    <li>Implemented anonymous carts.</li>
 </ul>

  <h3>1.0.0-M6</h3>

  <ul>
      <li>Usage of <a href="http://javamoney.java.net/">Java money</a> instead of custom implementation.</li>
      <li>Introduce {@link io.sphere.sdk.products.queries.ProductProjectionQuery}.</li>
      <li>Introduce {@link io.sphere.sdk.meta.QueryDocumentation} to document the query API.</li>
  </ul>

  <h3>1.0.0-M5</h3>

  <ul>
      <li>Fixed client shutdown problem.</li>
      <li>Put {@link io.sphere.sdk.models.MetaAttributes MetaAttributes} in common module and make it an interface.</li>
      <li>Add {@link io.sphere.sdk.products.ProductProjection#isPublished()}.</li>
      <li>Add {@link io.sphere.sdk.productdiscounts.ProductDiscount ProductDiscount} models.</li>
      <li>Add {@link io.sphere.sdk.categories.Category#getExternalId() external id fields and methods} for categories.</li>
      <li>Make {@link io.sphere.sdk.products.ProductCatalogData#getCurrent()} return an optional {@link io.sphere.sdk.products.ProductData}, since current should not be accessible if {@link io.sphere.sdk.products.ProductCatalogData#isPublished()} returns false.</li>
      <li>Make masterVariant in {@link io.sphere.sdk.products.ProductDraftBuilder} mandatory.</li>
  </ul>


  <h3>1.0.0-M4</h3>
  <ul>
      <li>Replacing joda time library with Java 8 DateTime API.</li>
      <li>Removing dependency to Google Guava.</li>
      <li>Rename artifact organization to {@code io.sphere.sdk.jvm}.</li>
      <li>Rename {@code JsonUtils.readObjectFromJsonFileInClasspath} to {@code JsonUtils.readObjectFromResource}.</li>
      <li>Reduced the number of SBT modules to speed up travis builds since the resolving of artifacts for every module is slow. In addition fewer JARs needs to be downloaded.</li>
      <li>Introduced {@link io.sphere.sdk.products.ProductProjection}s.</li>
      <li>Javadoc does contain a table of content box for h3 headings.</li>
  </ul>

  <h3>1.0.0-M3</h3>
  <ul>
      <li>The query model can now be accessed by it's Query class, e.g., {@link io.sphere.sdk.categories.queries.CategoryQuery#model()}.</li>
      <li>Added a {@link io.sphere.sdk.meta.GettingStarted Getting Started} page.</li>
      <li>Added a {@link io.sphere.sdk.meta.JvmSdkFeatures Features of the SDK} page.</li>
      <li>Addad a legacy Play Java client for Play Framework 2.2.x.</li>
      <li>Added {@link io.sphere.sdk.products.PriceBuilder}.</li>
      <li>Further null checks.</li>
      <li>Add a lot of a Javadoc, in general for the packages.</li>
      <li>{@link io.sphere.sdk.categories.CategoryTree#of(java.util.List)} instead of CategoryTreeFactory is to be used for creating a category tree.</li>
      <li>Move {@link io.sphere.sdk.models.AddressBuilder} out of the {@link io.sphere.sdk.models.Address} class.</li>
      <li>Performed a lot of renamings like the {@code requests} package to {@code http}</li>
      <li>Moved commands and queries to own packages for easier discovery.</li>
      <li>Introduced new predicates for inequality like {@link io.sphere.sdk.queries.StringQueryModel#isGreaterThanOrEquals(String)},
      {@link io.sphere.sdk.queries.StringQueryModel#isNot(String)},
      {@link io.sphere.sdk.queries.StringQueryModel#isNotIn(String, String...)} or {@link io.sphere.sdk.queries.StringQueryModel#isNotPresent()}.</li>
      <li>Introduced an unsafe way to create predicates from strings with {@link io.sphere.sdk.queries.Predicate#of(String)}.</li>
  </ul>

  <h3>1.0.0-M2</h3>
  <ul>
      <li>With the new reference expansion dsl it is possible to discover and create reference expansion paths for a query.</li>
      <li>All artifacts have the ivy organization {@code io.sphere.jvmsdk}.</li>
      <li>Migration from Google Guavas com.google.common.util.concurrent.ListenableFuture to Java 8 {@link java.util.concurrent.CompletableFuture}.</li>
      <li>Removed all Google Guava classes from the public API (internally still used).</li>
      <li>The logger is more fine granular controllable, for example the logger {@code sphere.products.responses.queries} logs only the responses of the queries for products. The trace level logs the JSON of responses and requests as pretty printed JSON.</li>
      <li>Introduced the class {@link io.sphere.sdk.models.Referenceable} which enables to use a model or a reference to a model as parameter, so no direct call of {@link io.sphere.sdk.models.DefaultModel#toReference()} is needed anymore for model classes.</li>
      <li>It is possible to overwrite the error messages of {@link io.sphere.sdk.test.DefaultModelAssert}, {@link io.sphere.sdk.test.LocalizedStringsAssert} and {@link io.sphere.sdk.test.ReferenceAssert}.</li>
      <li>{@link io.sphere.sdk.models.Versioned} contains a type parameter to prevent copy and paste errors.</li>
      <li>Sorting query model methods have better support in the IDE, important methods are bold.</li>
      <li>Queries and commands for models are in there own package now and less coupled to the model.</li>
      <li>The query classes have been refactored.</li>
  </ul>
 */
public final class ReleaseNotes {
    private ReleaseNotes() {
    }
}
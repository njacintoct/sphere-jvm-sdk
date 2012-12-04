package sphere;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.Futures;
import de.commercetools.internal.util.Log;
import de.commercetools.sphere.client.CommandRequest;
import de.commercetools.sphere.client.SphereException;
import de.commercetools.sphere.client.model.QueryResult;
import de.commercetools.sphere.client.shop.CommentService;
import de.commercetools.sphere.client.shop.CustomerService;
import de.commercetools.sphere.client.shop.OrderService;
import de.commercetools.sphere.client.shop.ReviewService;
import de.commercetools.sphere.client.shop.model.*;
import sphere.util.IdWithVersion;

import com.google.common.util.concurrent.ListenableFuture;
import net.jcip.annotations.ThreadSafe;

import javax.annotation.Nullable;

/** Project customer that is automatically associated to the current HTTP session.
 *
 *  After calling {@link sphere.SphereClient#logout()}, any existing CurrentCustomer instance is not valid any more
 *  and will throw {@link IllegalStateException}.
 *
 *  Therefore, don't keep instances of this class around, but always use {@link sphere.SphereClient#currentCustomer()}
 *  to get an up-to-date instance or null if no one is logged in.
 * */
@ThreadSafe
public class CurrentCustomer {
    private final Session session;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final CommentService commentService;
    private final ReviewService reviewService;

    private CurrentCustomer(Session session,
                            CustomerService customerService,
                            OrderService orderService,
                            CommentService commentService,
                            ReviewService reviewService) {
        this.session = session;
        this.customerService = customerService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    private IdWithVersion getIdWithVersion() {
        final IdWithVersion sessionCustomerId = session.getCustomerId();
        if (sessionCustomerId != null) {
            return sessionCustomerId;
        }
        throw new IllegalStateException(
                "This CurrentCustomer instance is not valid anymore. Please don't hold references to CurrentCustomer instances " +
                "after calling logout(). Instead, always use SphereClient.currentCustomer() to get an up-to-date instance or null.");
    }

    /** If a customer is logged in, returns a {@link CurrentCustomer} instance. If no customer is logged in, returns null. */
    public static CurrentCustomer createFromSession(CustomerService customerService,
                                                    OrderService orderService,
                                                    CommentService commentService,
                                                    ReviewService reviewService) {
        final Session session = Session.current();
        final IdWithVersion sessionCustomerId = session.getCustomerId();
        if (sessionCustomerId == null) {
            return null;
        }
        return new CurrentCustomer(session, customerService, orderService, commentService, reviewService);
    }

    /** Fetches the {@link Customer} from the server. The version number of the current customer is updated to the version
     *  of the returned customer. */
    public Customer fetch() {
        try {
            return fetchAsync().get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    public ListenableFuture<Customer> fetchAsync() {
        final IdWithVersion idWithVersion = getIdWithVersion();
        Log.trace(String.format("[customer] Fetching customer %s.", idWithVersion.id()));
        ListenableFuture<Customer> customerFuture = Futures.transform(customerService.byId(idWithVersion.id()).fetchAsync(), new Function<Optional<Customer>, Customer>() {
            public Customer apply(@Nullable Optional<Customer> customer) {
                assert customer != null;
                if (!customer.isPresent()) {
                    session.clearCustomer();  // the customer was probably deleted, clear it from this old session
                    throw new SphereException("Customer " + idWithVersion.id() + " no longer exists.");
                }
                return customer.get();
            }
        });
        return Session.withCustomerId(customerFuture, session);
    }


    /**
     * A helper method for {@link CustomerService#changePassword}
     *
     * @throws SphereException
     */
    public Customer changePassword(String currentPassword, String newPassword) {
        try {
            return changePasswordAsync(currentPassword, newPassword).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#changePassword}
     */
    public ListenableFuture<Customer> changePasswordAsync(String currentPassword, String newPassword){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.changePassword(idV.id(), idV.version(), currentPassword, newPassword),
                String.format("[customer] Changing password for customer %s.", idV.id()));
    }

    /**
     * A helper method for {@link CustomerService#changeShippingAddress}
     *
     * @throws SphereException
     */
    public Customer changeShippingAddress(int addressIndex, Address address) {
        try {
            return changeShippingAddressAsync(addressIndex, address).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#changeShippingAddress}
     */
    public ListenableFuture<Customer> changeShippingAddressAsync(int addressIndex, Address address){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.changeShippingAddress(idV.id(), idV.version(), addressIndex, address),
                String.format("[customer] Changing shipping address for customer %s.", idV.id()));
    }

    /**
     * A helper method for {@link CustomerService#removeShippingAddress}
     *
     * @throws SphereException
     */
    public Customer removeShippingAddress(int addressIndex) {
        try {
            return removeShippingAddressAsync(addressIndex).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#removeShippingAddress}
     */
    public ListenableFuture<Customer> removeShippingAddressAsync(int addressIndex){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.removeShippingAddress(idV.id(), idV.version(), addressIndex),
                String.format("[customer] Changing shipping address with index %s for customer %s.", addressIndex, idV.id()));
    }


    /**
     * A helper method for {@link CustomerService#setDefaultShippingAddress}
     *
     * @throws SphereException
     */
    public Customer setDefaultShippingAddress(int addressIndex) {
        try {
            return setDefaultShippingAddressAsync(addressIndex).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#setDefaultShippingAddress}
     */
    public ListenableFuture<Customer> setDefaultShippingAddressAsync(int addressIndex){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.setDefaultShippingAddress(idV.id(), idV.version(), addressIndex),
                String.format("[customer] Setting default shipping address with index %s for customer %s.", addressIndex, idV.id()));
    }

    /**
     * A helper method for {@link CustomerService#updateCustomer}
     *
     * @throws SphereException
     */
    public Customer updateCustomer(CustomerUpdate update) {
        try {
            return updateCustomerAsync(update).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#updateCustomer}
     */
    public ListenableFuture<Customer> updateCustomerAsync(CustomerUpdate update){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.updateCustomer(idV.id(), idV.version(), update),
                String.format("[customer] Updating customer %s.", idV.id()));
    }

    /**
     * A helper method for {@link CustomerService#resetPassword}
     *
     * @throws SphereException
     */
    public Customer resetPassword(String tokenValue, String newPassword) {
        try {
            return resetPasswordAsync(tokenValue, newPassword).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#resetPassword}
     */
    public ListenableFuture<Customer> resetPasswordAsync(String tokenValue, String newPassword){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.resetPassword(idV.id(), idV.version(), tokenValue, newPassword),
                String.format("[customer] Resetting password for customer %s.", idV.id()));
    }

    /**
     * A helper method for {@link CustomerService#createEmailVerificationToken}
     *
     * @throws SphereException
     */
    public CustomerToken createEmailVerificationToken(int ttlMinutes) {
        try {
            return createEmailVerificationTokenAsync(ttlMinutes).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#createEmailVerificationToken}
     */
    public ListenableFuture<CustomerToken> createEmailVerificationTokenAsync(int ttlMinutes){
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Creating email verification token for customer %s.", idV.id()));
        return customerService.createEmailVerificationToken(idV.id(), idV.version(), ttlMinutes).executeAsync();
    }

    /**
     * A helper method for {@link CustomerService#verifyEmail}
     *
     * @throws SphereException
     */
    public Customer verifyEmail(String tokenValue) {
        try {
            return verifyEmailAsync(tokenValue).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CustomerService#verifyEmail}
     */
    public ListenableFuture<Customer> verifyEmailAsync(String tokenValue){
        final IdWithVersion idV = getIdWithVersion();
        return executeAsync(
                customerService.verifyEmail(idV.id(), idV.version(), tokenValue),
                String.format("[customer] Verifying email for customer %s.", idV.id()));
    }

    /**
     * A helper method for {@link OrderService#byCustomerId}
     *
     * @throws SphereException
     */
    public QueryResult<Order> getOrders() {
        try {
            return getOrdersAsync().get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link OrderService#byCustomerId}
     */
    public ListenableFuture<QueryResult<Order>> getOrdersAsync() {
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Getting orders of customer %s.", idV.id()));
        return orderService.byCustomerId(idV.id()).fetchAsync();
    }


    // --------------------------------------
    // Comments and Reviews
    // --------------------------------------

    /**
     * A helper method for {@link ReviewService#byCustomerId}
     *
     * @throws SphereException
     */
    public QueryResult<Review> getReviews() {
        try {
            return getReviewsAsync().get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link ReviewService#byCustomerId}
     */
    public ListenableFuture<QueryResult<Review>> getReviewsAsync() {
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Getting reviews of customer %s.", idV.id()));
        return reviewService.byCustomerId(idV.id()).fetchAsync();
    }

    /**
     * A helper method for {@link ReviewService#byCustomerIdProductId}
     *
     * @throws SphereException
     */
    public QueryResult<Review> getReviewsByProductId(String productId) {
        try {
            return getReviewsByProductIdAsync(productId).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link ReviewService#byCustomerIdProductId}
     */
    public ListenableFuture<QueryResult<Review>> getReviewsByProductIdAsync(String productId) {
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Getting reviews of customer %s on a product.", idV.id(), productId));
        return reviewService.byCustomerIdProductId(idV.id(), productId).fetchAsync();
    }

    /**
     * A helper method for {@link ReviewService#createReview}
     *
     * @throws SphereException
     */
    public Review createReview(String productId, String authorName, String title, String text, Double score) {
        try {
            return createReviewAsync(productId, authorName, title, text, score).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link ReviewService#createReview}
     */
    public ListenableFuture<Review> createReviewAsync(String productId, String authorName, String title, String text, Double score) {
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Creating a review for customer %s.", idV.id()));
        return reviewService.createReview(productId, idV.id(), authorName, title, text, score).executeAsync();
    }

    /**
     * A helper method for {@link CommentService#byCustomerId}
     *
     * @throws SphereException
     */
    public QueryResult<Comment> getComments() {
        try {
            return getCommentsAsync().get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CommentService#byCustomerId}
     */
    public ListenableFuture<QueryResult<Comment>> getCommentsAsync() {
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Getting comments of customer %s.", idV.id()));
        return commentService.byCustomerId(idV.id()).fetchAsync();
    }

    /**
     * A helper method for {@link CommentService#createComment}
     *
     * @throws SphereException
     */
    public Comment createComment(String productId, String authorName, String title, String text) {
        try {
            return createCommentAsync(productId, authorName, title, text).get();
        } catch(Exception e) {
            throw new SphereException(e);
        }
    }

    /**
     * A helper method for {@link CommentService#createComment}
     */
    public ListenableFuture<Comment> createCommentAsync(String productId, String authorName, String title, String text) {
        final IdWithVersion idV = getIdWithVersion();
        Log.trace(String.format("[customer] Creating a comment for customer %s.", idV.id()));
        return commentService.createComment(productId, idV.id(), authorName, title, text).executeAsync();
    }



    // --------------------------------------
    // Command helpers
    // --------------------------------------

    private ListenableFuture<Customer> executeAsync(CommandRequest<Customer> commandRequest, String logMessage) {
        Log.trace(logMessage);
        return Session.withCustomerId(commandRequest.executeAsync(), session);
    }

}
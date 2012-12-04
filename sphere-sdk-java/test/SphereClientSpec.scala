package sphere

import de.commercetools.sphere.client.shop.{CartService, CustomerService, ShopClient}
import de.commercetools.sphere.client.SphereException
import com.google.common.base.Optional

class SphereClientSpec extends ServiceSpec {

  def sphereClient(customerService: CustomerService = null, cartService: CartService = null): SphereClient = {
    val config = mock[Config]
    config stubs 'shopCurrency returning EUR
    val shopClient = new ShopClient(null, null, null, cartService, null, customerService, null, null)
    new SphereClient(config, shopClient)
  }

  def sessionCustomerUpdated() {
    val idVer = Session.current().getCustomerId
    idVer.id() must be (resultCustomer.id)
    idVer.version() must be (resultCustomer.version)
  }

  def sessionCartUpdated() {
    val idVer = Session.current().getCartId
    idVer.id() must be (resultTestCart.id)
    idVer.version() must be (resultTestCart.version)
  }

  "signup()" must {
    "invoke customerService.signup() if no cart exists in the session and put customer id and version into session" in {
      val customerService = customerServiceExpectingCommand(
        'signup, List("em@ail.com", "secret", "hans", "wurst", "hungry", "the dude"),
        resultCustomer)
      sphereClient(customerService).signup("em@ail.com", "secret", "hans", "wurst", "hungry", "the dude")
      sessionCustomerUpdated()
      Session.current().getCartId must be (null)
    }
    "invoke customerService.signupWithCart() if a cart exists in the session and put customer and cart id and version into session" in {
      Session.current().putCart(testCart)
      val customerService = customerServiceExpectingCommand(
        'signupWithCart, List("em@ail.com", "secret", "hans", "wurst", "hungry", "the dude", testCart.id, testCart.version),
        loginResultWithCart)
      sphereClient(customerService).signup("em@ail.com", "secret", "hans", "wurst", "hungry", "the dude")
      sessionCustomerUpdated()
      sessionCartUpdated()
    }
  }

  "login()" must {
    "invoke customerService.login() without anonymous cart in session and put customer id and version into session" in {
      val customerService = customerServiceExpectingFetch(
        'byCredentials, List("em@ail.com", "secret"),
        Optional.of(loginResultNoCart))
      sphereClient(customerService).login("em@ail.com", "secret")
      sessionCustomerUpdated()
      Session.current().getCartId must be (null)
    }
    "invoke cartService.loginWithAnonymousCart() and put customer and cart id and version into session" in {
      Session.current().putCart(testCart)
      val cartService = cartServiceExpectingCommand(
        'loginWithAnonymousCart, List(testCart.id, testCart.version, "em@ail.com", "secret"),
        Optional.of(loginResultWithCart))
      sphereClient(cartService = cartService).login("em@ail.com", "secret")
      sessionCustomerUpdated()
      sessionCartUpdated()
    }
  }

  "currentCustomer()" must {
    "return null on empty session" in {
      sphereClient(null).currentCustomer() must be (null)
    }
    "return CurrentCustomer when session contains customer id" in {
      Session.current().putCustomer(initialCustomer)
      sphereClient(null).currentCustomer() != null must be (true)
    }
  }

  "logout()" must {
    "remove customer and cart data from the session" in {
      Session.current().putCustomer(initialCustomer)
      Session.current().putCart(testCart)
      val initialSession = Session.current()
      initialSession.getCustomerId != null must be (true)
      initialSession.getCartId != null must be (true)

      sphereClient(null).logout()
      val updatedSession = Session.current()
      updatedSession.getCustomerId must be (null)
      updatedSession.getCartId must be (null)
    }
    "throw illegal state expection on CurrentCustomer methods if invoked after logout" in {
      Session.current().putCustomer(initialCustomer)
      val currentCustomer = sphereClient(null).currentCustomer()
      sphereClient(null).logout()
      try {
        currentCustomer.changePassword("","")
        fail("exception expected.")
      } catch {
        case e: SphereException => e.getCause.isInstanceOf[IllegalStateException] must be (true)
      }
    }
  }
}
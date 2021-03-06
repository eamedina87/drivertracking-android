package tech.medina.drivertracking.ui.navigation

import android.content.Intent
import android.os.Bundle
import tech.medina.drivertracking.ui.base.BaseActivity
import tech.medina.drivertracking.ui.delivery.detail.DeliveryDetailActivity
import tech.medina.drivertracking.ui.delivery.detail.DeliveryDetailFragment
import tech.medina.drivertracking.ui.dialog.DialogWithTwoOptions
import tech.medina.drivertracking.ui.utils.Constants.INTENT_EXTRA_DELIVERY_ID
import tech.medina.drivertracking.ui.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {

    fun goToActivity(source: BaseActivity,
                     destination: Activity,
                     extras: Bundle? = null,
                     requestCode: Int? = null,
                     finish: Boolean = false) {

        if (destination is Activity.NoActivity) return

        val intent = Intent(source, destination.className)

        extras?.let {
            intent.putExtras(extras)
        }

        if (requestCode == null) {
            source.startActivity(intent)
        } else {
            source.startActivityForResult(intent, requestCode)
        }

        if (finish) source.finish()

    }

    fun showTwoOptionsDialog(activity: BaseActivity,
                             title: String? = null,
                             message: String,
                             leftButtonText: String? = null,
                             rightButtonText: String? = null,
                             leftButtonFunction: (() -> Unit)? = null,
                             rightButtonFunction: (() -> Unit)? = null) {
       DialogWithTwoOptions(title, message, leftButtonText, rightButtonText,
            leftButtonFunction, rightButtonFunction).
        show(activity.supportFragmentManager, "twoOptionsDialog")
    }

    fun goToDetail(source: BaseActivity, deliveryId: Long, containerId: Int) {
        val extras = Bundle().apply {
            putLong(INTENT_EXTRA_DELIVERY_ID, deliveryId)
        }
        if (Utils.isTablet(source)) {
            source.replaceFragment(
                containerViewId = containerId,
                fragment = DeliveryDetailFragment.createWithExtras(extras),
                tag = "delivery.detail")
        } else {
            goToActivity(
                source = source,
                destination = Activity.DeliveryDetail,
                extras = extras)
        }
    }

    sealed class Activity(val className: Class<*>) {
        object NoActivity: Activity(Any::class.java)
        object DeliveryDetail: Activity(DeliveryDetailActivity::class.java)
    }

}
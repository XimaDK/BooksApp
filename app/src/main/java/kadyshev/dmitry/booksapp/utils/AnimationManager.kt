package kadyshev.dmitry.booksapp.utils

import android.animation.ObjectAnimator
import android.view.View

object AnimationManager {

    fun rotateIndefinitely(view: View) {
        val rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        rotateAnimator.duration = 700
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.start()
    }

    fun stopRotation(view: View) {
        view.clearAnimation()
    }

}
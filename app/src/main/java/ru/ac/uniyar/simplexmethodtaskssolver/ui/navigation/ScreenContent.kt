package ru.ac.uniyar.simplexmethodtaskssolver.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.childAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.animation.child.slide
import ru.ac.uniyar.simplexmethodtaskssolver.ui.screens.ArtificialBasisScreen
import ru.ac.uniyar.simplexmethodtaskssolver.ui.screens.GraphicScreen
import ru.ac.uniyar.simplexmethodtaskssolver.ui.screens.MainScreen
import ru.ac.uniyar.simplexmethodtaskssolver.ui.screens.SimplexScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun Content(router: MainRouter) {
    Children(
        routerState = router.routerState,
        animation = childAnimation(slide()),
    ) {
        when (val child = it.instance) {
            is MainRouter.Child.Main -> MainScreen(child.component)
            is MainRouter.Child.ArtificialBasis -> ArtificialBasisScreen(child.component)
            is MainRouter.Child.Graphic -> GraphicScreen(child.component)
            is MainRouter.Child.Simplex -> SimplexScreen(child.component)
        }
    }
}

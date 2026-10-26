package com.example.proyectoappsmobiles.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectoappsmobiles.data.local.AppDatabase
import com.example.proyectoappsmobiles.data.repository.AppRepository
import com.example.proyectoappsmobiles.ui.docente.DocenteScreen
import com.example.proyectoappsmobiles.ui.docente.DocenteViewModel
import com.example.proyectoappsmobiles.ui.estudiante.EstudianteActivityScreen
import com.example.proyectoappsmobiles.ui.estudiante.EstudianteContentScreen
import com.example.proyectoappsmobiles.ui.estudiante.EstudianteHomeScreen
import com.example.proyectoappsmobiles.ui.estudiante.EstudianteTestScreen
import com.example.proyectoappsmobiles.ui.estudiante.EstudianteViewModel
import com.example.proyectoappsmobiles.ui.login.LoginScreen
import com.example.proyectoappsmobiles.ui.login.LoginViewModel
import com.example.proyectoappsmobiles.ui.utp.UtpScreen
import com.example.proyectoappsmobiles.ui.utp.UtpViewModel

object Routes {
    const val LOGIN = "login"
    const val UTP = "utp"
    const val DOCENTE = "docente"
    const val ESTUDIANTE_HOME = "estudiante_home/{studentId}"
    const val ESTUDIANTE_CONTENT = "estudiante_content"
    const val ESTUDIANTE_ACTIVITIES = "estudiante_activities"
    const val ESTUDIANTE_TEST = "estudiante_test"
}

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(UtpViewModel::class.java) -> UtpViewModel(repository) as T
            modelClass.isAssignableFrom(DocenteViewModel::class.java) -> DocenteViewModel(repository) as T
            modelClass.isAssignableFrom(EstudianteViewModel::class.java) -> EstudianteViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val repository = remember {
        val db = AppDatabase.getInstance(context)
        AppRepository(db)
    }

    val factory = remember { ViewModelFactory(repository) }

    val loginViewModel: LoginViewModel = viewModel(factory = factory)
    val utpViewModel: UtpViewModel = viewModel(factory = factory)
    val docenteViewModel: DocenteViewModel = viewModel(factory = factory)
    val estudianteViewModel: EstudianteViewModel = viewModel(factory = factory)

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToUtp = { navController.navigate(Routes.UTP) },
                onNavigateToDocente = { navController.navigate(Routes.DOCENTE) },
                onNavigateToEstudiante = { studentId ->
                    estudianteViewModel.loadStudent(studentId)
                    navController.navigate("estudiante_home/$studentId")
                }
            )
        }

        composable(Routes.UTP) {
            UtpScreen(
                viewModel = utpViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DOCENTE) {
            DocenteScreen(
                viewModel = docenteViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ESTUDIANTE_HOME,
            arguments = listOf(navArgument("studentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: "est_01"
            estudianteViewModel.loadStudent(studentId)

            EstudianteHomeScreen(
                viewModel = estudianteViewModel,
                onNavigateToContent = { navController.navigate(Routes.ESTUDIANTE_CONTENT) },
                onBackToLogin = { navController.popBackStack(Routes.LOGIN, false) }
            )
        }

        composable(Routes.ESTUDIANTE_CONTENT) {
            EstudianteContentScreen(
                viewModel = estudianteViewModel,
                onNavigateToActivities = { navController.navigate(Routes.ESTUDIANTE_ACTIVITIES) },
                onNavigateToTest = { navController.navigate(Routes.ESTUDIANTE_TEST) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ESTUDIANTE_ACTIVITIES) {
            EstudianteActivityScreen(
                viewModel = estudianteViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ESTUDIANTE_TEST) {
            EstudianteTestScreen(
                viewModel = estudianteViewModel,
                onBack = { navController.popBackStack(Routes.ESTUDIANTE_CONTENT, false) }
            )
        }
    }
}

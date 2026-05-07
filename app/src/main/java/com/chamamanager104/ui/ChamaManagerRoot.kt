package com.chamamanager104.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
//import androidx.navigation.findStartDestination
import com.chamamanager104.core.model.UserRole
import com.chamamanager104.core.model.canManageFinance
import com.chamamanager104.core.model.canManageMembers
import com.chamamanager104.core.model.displayName
import com.chamamanager104.ui.feature.auth.AuthViewModel
import com.chamamanager104.ui.feature.auth.ForgotPasswordScreen
import com.chamamanager104.ui.feature.auth.LoginScreen
import com.chamamanager104.ui.feature.auth.SignupScreen
import com.chamamanager104.ui.feature.contributions.ContributionsScreen
import com.chamamanager104.ui.feature.contributions.ContributionsViewModel
import com.chamamanager104.ui.feature.dashboard.DashboardScreen
import com.chamamanager104.ui.feature.dashboard.DashboardViewModel
import com.chamamanager104.ui.feature.loans.LoansScreen
import com.chamamanager104.ui.feature.loans.LoansViewModel
import com.chamamanager104.ui.feature.meetings.MeetingsScreen
import com.chamamanager104.ui.feature.meetings.MeetingsViewModel
import com.chamamanager104.ui.feature.members.MembersScreen
import com.chamamanager104.ui.feature.members.MembersViewModel
import com.chamamanager104.ui.feature.notifications.NotificationsScreen
import com.chamamanager104.ui.feature.notifications.NotificationsViewModel
import com.chamamanager104.ui.feature.onboarding.OnboardingScreen
import com.chamamanager104.ui.feature.reports.ReportsScreen
import com.chamamanager104.ui.navigation.NavRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChamaManagerRoot(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerExpanded = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val userSession by appViewModel.userSession.collectAsStateWithLifecycle()
    val role = userSession?.role ?: UserRole.MEMBER
    val authenticated = userSession != null

//    val startDestination = if (authenticated) NavRoute.Dashboard.route else NavRoute.Login.route/// changed the start destination to onboarding screen
    val startDestination = if (authenticated) NavRoute.Dashboard.route else NavRoute.Onboarding.route

    val destinations = buildList {
        add(DrawerDestination(NavRoute.Dashboard, "Dashboard", Icons.Outlined.Home))
        if (role.canManageMembers()) add(DrawerDestination(NavRoute.Members, "Members", Icons.Outlined.Group))
        if (role.canManageFinance()) {
            add(DrawerDestination(NavRoute.Contributions, "Contributions", Icons.Outlined.VolunteerActivism))
            add(DrawerDestination(NavRoute.Loans, "Loans", Icons.Outlined.AccountBalance))
        }
        add(DrawerDestination(NavRoute.Meetings, "Meetings", Icons.Outlined.Today))
        add(DrawerDestination(NavRoute.Reports, "Reports", Icons.Outlined.ReceiptLong))
        add(DrawerDestination(NavRoute.Notifications, "Notifications", Icons.Outlined.Notifications))
    }

    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    LaunchedEffect(authenticated) {
        if (authenticated) {
            navController.navigate(NavRoute.Dashboard.route) {
//                popUpTo(NavRoute.Login.route) { inclusive = true } /// removed to add below
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }

                launchSingleTop = true
            }
        } else {
//            navController.navigate(NavRoute.Login.route) { ///removed to add below
            navController.navigate(NavRoute.Onboarding.route) {

            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = authenticated,
        drawerContent = {
            if (authenticated && userSession != null) {
                DrawerContent(
                    role = role,
                    chamaName = userSession?.chamaName.orEmpty(),
                    inviteCode = userSession?.chamaCode.orEmpty(),
                    currentRoute = currentRoute,
                    destinations = destinations,
                    expanded = drawerExpanded.value,
                    onToggleExpanded = { drawerExpanded.value = !drawerExpanded.value },
                    onSelectRoute = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                ),
            topBar = {
                if (authenticated) {
                    TopAppBar(
                        title = {
                            Column {
                                Text(screenTitle(currentRoute))
                                Text(
                                    "${userSession?.chamaName.orEmpty()} - ${role.displayName()}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(Icons.Outlined.Menu, contentDescription = "Open sidebar")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(padding)
            ) {
                /// added
                composable(NavRoute.Onboarding.route) {
                    OnboardingScreen( /// added an importation
                        onContinue = {
                            navController.navigate(NavRoute.Login.route) {
                                popUpTo(NavRoute.Onboarding.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                /// added
                composable(NavRoute.Login.route) {
                    val viewModel: AuthViewModel = hiltViewModel()
                    LoginScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onLogin = viewModel::login,
                        onNavigateToSignup = { navController.navigate(NavRoute.Signup.route) },
                        onNavigateToForgotPassword = { navController.navigate(NavRoute.ForgotPassword.route) }
                    )
                }
                composable(NavRoute.Signup.route) {
                    val viewModel: AuthViewModel = hiltViewModel()
                    SignupScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onSignup = viewModel::signup,
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }
                composable(NavRoute.ForgotPassword.route) {
                    val viewModel: AuthViewModel = hiltViewModel()
                    ForgotPasswordScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onSendReset = viewModel::sendPasswordReset,
                        onNavigateToLogin = { navController.popBackStack() }
                    )
                }
                composable(NavRoute.Dashboard.route) {
                    val viewModel: DashboardViewModel = hiltViewModel()
                    DashboardScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        role = role,
                        onSignOut = appViewModel::signOut
                    )
                }
                composable(NavRoute.Members.route) {
                    val viewModel: MembersViewModel = hiltViewModel()
                    MembersScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        role = role,
                        onSave = viewModel::saveMember,
                        onDelete = viewModel::deleteMember
                    )
                }
                composable(NavRoute.Contributions.route) {
                    val viewModel: ContributionsViewModel = hiltViewModel()
                    ContributionsScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onRecord = viewModel::recordContribution
                    )
                }
                composable(NavRoute.Loans.route) {
                    val viewModel: LoansViewModel = hiltViewModel()
                    LoansScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onSave = viewModel::saveLoan
                    )
                }
                composable(NavRoute.Meetings.route) {
                    val viewModel: MeetingsViewModel = hiltViewModel()
                    MeetingsScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onSave = viewModel::saveMeeting
                    )
                }
                composable(NavRoute.Reports.route) {
                    ReportsScreen()
                }
                composable(NavRoute.Notifications.route) {
                    val viewModel: NotificationsViewModel = hiltViewModel()
                    NotificationsScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value
                    )
                }
            }
        }
    }
}

private data class DrawerDestination(
    val route: NavRoute,
    val label: String,
    val icon: ImageVector
)

@Composable
private fun DrawerContent(
    role: UserRole,
    chamaName: String,
    inviteCode: String,
    currentRoute: String?,
    destinations: List<DrawerDestination>,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onSelectRoute: (String) -> Unit
) {
    ModalDrawerSheet(modifier = Modifier.width(if (expanded) 312.dp else 104.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(26.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (expanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Chama Manager", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                            Text(chamaName, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Code: $inviteCode", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(role.displayName(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    } else {
                        Text("CM", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onToggleExpanded) {
                        Icon(
                            imageVector = if (expanded) Icons.Outlined.ChevronLeft else Icons.Outlined.Menu,
                            contentDescription = if (expanded) "Collapse sidebar" else "Expand sidebar"
                        )
                    }
                }
            }

            destinations.forEach { destination ->
                NavigationDrawerItem(
                    selected = currentRoute == destination.route.route,
                    onClick = { onSelectRoute(destination.route.route) },
                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                    label = {
                        if (expanded) {
                            Text(destination.label)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SectionColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = { content() }
    )
}

val DefaultInsets = PaddingValues(16.dp)

private fun screenTitle(route: String?): String = when (route) {
    NavRoute.Dashboard.route -> "Dashboard"
    NavRoute.Members.route -> "Members"
    NavRoute.Contributions.route -> "Contributions"
    NavRoute.Loans.route -> "Loans"
    NavRoute.Meetings.route -> "Meetings"
    NavRoute.Reports.route -> "Reports"
    NavRoute.Notifications.route -> "Notifications"
    else -> "Chama Manager 104"
}

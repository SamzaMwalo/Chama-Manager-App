package com.chamamanager104.ui.navigation

sealed class NavRoute(val route: String) {
    data object Login : NavRoute("login")
    data object Signup : NavRoute("signup")
    data object ForgotPassword : NavRoute("forgot_password")
    data object Onboarding : NavRoute("onboarding")
    data object Dashboard : NavRoute("dashboard")
    data object Members : NavRoute("members")
    data object Contributions : NavRoute("contributions")
    data object Loans : NavRoute("loans")
    data object Meetings : NavRoute("meetings")
    data object Reports : NavRoute("reports")
    data object Notifications : NavRoute("notifications")
}

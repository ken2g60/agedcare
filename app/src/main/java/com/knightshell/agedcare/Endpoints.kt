package com.knightshell.agedcare

object Endpoints {
    const val BLOG_JSON_URL = "https://agedcare.herokuapp.com/api/v1/blog/post/?format=json"
    const val USER_AUTHENTICATION_LOGIN_ENDPOINT = "https://agedcare.herokuapp.com/api-token-auth/"
    const val USER_AUTHENTICATION_REGISTER_ENDPOINT = "https://agedcare.herokuapp.com/api/v1/user/signup"
    const val USER_GLUCOSE_ENDPOINT = "https://agedcare.herokuapp.com/api/v1/health/glucose/"
    const val USER_PRESSURE_ENDPOINT = "https://agedcare.herokuapp.com/api/v1/health/pressure/"
    const val USER_WEIGHT_ENDPOINT = "https://agedcare.herokuapp.com/api/v1/health/weight/"
    const val PAYMENT_ENDPOINT = "https://agedcare.herokuapp.com/api/v1/user/payment"
    const val USER_AUTHENTICATION_RESET_ENDPOINT = ""
}
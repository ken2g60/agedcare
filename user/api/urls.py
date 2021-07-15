from .views import CreateUserView, PaymentAPIView, SubscriptionAPIView
from django.urls import include, path
from . import views 

urlpatterns = [
    path("signup", CreateUserView.as_view(),name="signup"),
    path("payment", PaymentAPIView.as_view(), name="payment"),
    path("subscription", SubscriptionAPIView.as_view(), name="subscription"),
    path("webhook", views.webhook, name="webhook"),
]
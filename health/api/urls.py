from django.urls import path 
from health.api.views import HealthAPIView, GlucoseAPIView, PressureAPIView, WeightAPIView


urlpatterns = [
    path('bulk/', HealthAPIView.as_view()),
    path('glucose/', GlucoseAPIView.as_view()),
    path('pressure/', PressureAPIView.as_view()),
    path('weight/', WeightAPIView.as_view()),
]
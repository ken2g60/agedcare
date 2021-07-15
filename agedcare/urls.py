from django.contrib import admin
from django.urls import path
from django.conf.urls import include, url

from agedcare_ussd.views import Hubtel, AfricaTalking

from django.conf import settings
from django.conf.urls.static import static
from rest_framework.authtoken.views import obtain_auth_token


admin.site.site_header = "AgedCare Admin"
admin.site.site_title = "AgedCare"
admin.site.index_title = "Welcome to AgedCare Portal"

urlpatterns = [
    path('admin/', admin.site.urls),
    url(r'^api-auth/', include('rest_framework.urls')),
    path('rest-auth/', include('rest_auth.urls')),
    path('api/v1/user/', include('user.api.urls')),
    path('api/v1/blog/', include('post.api.urls')),
    path('api/v1/health/', include('health.api.urls')),
    url(r'hubtel/', Hubtel.as_view()),
	url(r'africatalking/', AfricaTalking.as_view()),
    url(r'^admin/statuscheck/', include('celerybeat_status.urls'), name='celerybeat_status'),
    path('api-token-auth/', obtain_auth_token, name='api_token_auth'),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)

urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)

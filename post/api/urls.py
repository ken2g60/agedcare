from django.urls import path 
from post.api.views import PostAPIView
from django.conf import settings
from django.conf.urls.static import static 


urlpatterns = [
    path('post/', PostAPIView.as_view())
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)

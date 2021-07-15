from django.contrib import admin
from .models import Post



class PostAdmin(admin.ModelAdmin):
    list_display = ('id', 'title', 'image', 'description', 'created_at')

admin.site.register(Post, PostAdmin)
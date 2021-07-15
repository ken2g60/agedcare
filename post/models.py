from django.db import models
from django.utils.translation import gettext as _

class Post(models.Model):
    title = models.CharField(_("Blog Title"), max_length=50)
    image = models.ImageField(_("Blog Image"), upload_to='images/')
    description = models.TextField(blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    
    
from rest_framework import serializers
from post.models import Post

class PostSerializer(serializers.Serializer):
    title = serializers.CharField()
    image = serializers.ImageField(max_length=None, use_url=True, allow_null=True, required=False)
    description = serializers.CharField()
    created_at = serializers.DateTimeField()
    
    class Meta:
        model = Post
        fields = ('title', 'image', 'description')
  
from django.contrib.auth.models import User
from rest_framework import authentication
from rest_framework import exceptions

from user.models import CustomUser


class CustomAuthentication(authentication.BaseAuthentication):
    
    
    def authenticate(self, request):
        phonenumber = request.GET.get('X_USERNAME')
        print(phonenumber)
        password = request.GET.get('password')
        
        if not phonenumber:
            return None

            
        try:
            user = CustomUser.objects.get(phonenumber=phonenumber)
        except CustomUser.DoesNotExist:
            raise exceptions.AuthenticationFailed('No such account')
        
        return (user, None)
        
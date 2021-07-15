import json
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.generics import CreateAPIView
from django.contrib.auth import get_user_model 
from rest_framework.views import APIView
from user.api.serializers import PaymentSerializer, UserSerializer, SubscriptionSerializer
from rest_framework import status
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.decorators.http import require_POST


from user.models import Payment, CustomUser, Subscription
from sentry_sdk import capture_exception, capture_message

class CreateUserView(CreateAPIView):
    
    model = get_user_model()
    permission_classes = [
        permissions.AllowAny
    ]
    serializer_class = UserSerializer


class PaymentAPIView(APIView):
    
    def get(self, request):
        user_phonenumber  = Payment.objects.filter(phonenumber=request.user)
        serializer = PaymentSerializer(user_phonenumber, many=True)
        return Response(serializer.data)
        
    
    def post(self, request, *args, **kwargs):
        serializer = PaymentSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    

class SubscriptionAPIView(APIView):
    
    def get(self, request):
        user_subscription  = Subscription.objects.filter(phonenumber=request.user)
        serializer = SubscriptionSerializer(user_subscription, many=True)
        return Response(serializer.data)
        
    
    def post(self, request, *args, **kwargs):
        serializer = SubscriptionSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    


@csrf_exempt
@require_POST
def webhook(request):
    request_json = request.body
    response = json.loads(request_json)
    
    if response['status_code'] == '481':
        print("Transaction Failed")
        # insufficient_funds(response["source"]["number"])

    if response['status_code'] == '100':
        phonenumber = response["source"]["number"]
        firstname = response["first_name"] 
        lastname = response["last_name"]
        amount = response["amount"]
        
        
        try:
            user = CustomUser.objects.filter(phonenumber=phonenumber)
            if user.exists():
                Payment.objects.create(amount=amount,first_name=firstname,last_name=lastname,phonenumber=phonenumber,paid_status=True)
        except Exception as e:
            capture_exception(e)
        
    return HttpResponse(status=200)
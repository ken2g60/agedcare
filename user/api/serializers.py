from rest_framework import serializers
from django.contrib.auth import get_user_model

UserModel = get_user_model()

from user.models import Payment, Subscription

# asoriba payment
from django.utils import timezone
from datetime import datetime, timedelta
from sentry_sdk import capture_exception

from dateutil.relativedelta import *


class UserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)

    
    def create(self, validated_data):
        user = UserModel.objects.create(phonenumber=validated_data['phonenumber'],email=validated_data['email'])
        user.set_password(validated_data['password'])
        user.save()
        
        return user
    
    class Meta:
        model = UserModel
        fields = ("phonenumber", "email", "password")



class SubscriptionSerializer(serializers.Serializer):
    package_type = serializers.CharField()
    phonenumber = serializers.CharField()
    subscription_type = serializers.CharField()
    
    
    
    class Meta:
        model = Subscription
        fields = ("phonenumber", "subscription_type")
        
    

    def save(self):
        if self.validated_data['subscription_type'] == "monthly":
            current_datetime = datetime.now()
            end_date = current_datetime + relativedelta(months=+1)
            Subscription.objects.create(
                phonenumber=self.validated_data['phonenumber'],
                start_date=current_datetime,
                end_date=end_date,
                subscription_type='monthly',
                package_type=self.validated_data['package_type'],
                is_active=True
            )
        if self.validated_data['subscription_type'] == "three_months":
            current_datetime = datetime.now()
            end_date = current_datetime + relativedelta(days=93)
            Subscription.objects.create(
                phonenumber=self.validated_data['phonenumber'],
                start_date=current_datetime,
                end_date=end_date,
                subscription_type='three_months',
                package_type=self.validated_data['package_type'],
                is_active=True
            )
        
        if self.validated_data['subscription_type'] == "six_months":
            current_datetime = datetime.now()
            end_date = current_datetime + relativedelta(months=+6)
            Subscription.objects.create(
                phonenumber=self.validated_data['phonenumber'],
                start_date=current_datetime,
                end_date=end_date,
                subscription_type="six_months",
                package_type=self.validated_data['package_type'],
                is_active=True
            )
        
        if self.validated_data['subscription_type'] == "annual":
            current_datetime = datetime.now()
            end_date = current_datetime + timedelta(days=365)
            Subscription.objects.create(
                phonenumber=self.validated_data['phonenumber'],
                start_date=current_datetime,
                end_date=end_date,
                subscription_type="annual",
                package_type=self.validated_data['package_type'],
                is_active=True
            )
    
class PaymentSerializer(serializers.Serializer):
    amount = serializers.DecimalField(max_digits=10, decimal_places=2, coerce_to_string=False)
    first_name = serializers.CharField()
    last_name = serializers.CharField()
    phonenumber = serializers.CharField()
    email = serializers.EmailField(allow_null=True)
    
    
    class Meta:
        model = Payment
        fields = ("amount", "first_name", "last_name", "phonenumber", "email")
        
    def create(self, validated_data):
        return Payment.objects.create(**validated_data)
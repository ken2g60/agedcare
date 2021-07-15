from rest_framework import serializers
from health.models import HealthData, Glucose, Pressure, Weight
from careapp.models import PersonalDetail

class HealthDataSerializer(serializers.Serializer):
    userId = serializers.CharField()
    phonenumber = serializers.CharField()
    bloodsugar = serializers.IntegerField()
    bloodpressure = serializers.IntegerField()
    bloodcholesterol = serializers.IntegerField()
    bloodlevel = serializers.IntegerField()
    weight = serializers.models.CharField()
    created_at = serializers.DateTimeField()
    
    
    
    def create(self, validated_data):
        return HealthData.objects.create(**validated_data)
    
    
class GlucoseSerializer(serializers.Serializer):
    username = serializers.CharField()
    glucose = serializers.CharField()
    time = serializers.CharField()
    date = serializers.CharField()
    
    def create(self, validated_data):
        return Glucose.objects.create(**validated_data)


class PressureSerializer(serializers.Serializer):
    username = serializers.CharField()
    systolic = serializers.IntegerField()
    diastolic = serializers.IntegerField()
    time = serializers.CharField()
    date = serializers.CharField()
    
    
    def create(self, validated_data):
        return Pressure.objects.create(**validated_data)


class WeightSerializer(serializers.Serializer):
    username = serializers.CharField()
    weight = serializers.CharField()
    time = serializers.CharField()
    date = serializers.CharField()
    
    
    def create(self, validated_data):
        return Weight.objects.create(**validated_data)
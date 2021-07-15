from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import status
from health.api.serializers import HealthDataSerializer, GlucoseSerializer, PressureSerializer, WeightSerializer
from health.models import HealthData, Glucose, Pressure, Weight




class HealthAPIView(APIView):
    
    def get(self, request):
        health = HealthData.objects.all()
        serializer = HealthDataSerializer(health, many=True)
        return Response(serializer.data)
    
    
    def post(self, request, *args, **kwargs):
        serializer = HealthDataSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class GlucoseAPIView(APIView):
    def get(self, request):
        glucose = Glucose.objects.all()
        serializer = GlucoseSerializer(glucose, many=True)
        return Response(serializer.data)
    
    
    def post(self, request, *args, **kwargs):
        serializer = GlucoseSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST) 

class PressureAPIView(APIView):
    def get(self, request):
        pressure = Pressure.objects.all()
        serializer = PressureSerializer(pressure, many=True)
        return Response(serializer.data)
    
    def post(self, request, *args, **kwargs):
        serializer = PressureSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST) 


class WeightAPIView(APIView):
    
    def get(self, request):
        weight = Weight.objects.all()
        serializer = WeightSerializer(weight, many=True)
        return Response(serializer.data)
    
    def post(self, request, *args, **kwargs):
        serializer = WeightSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST) 
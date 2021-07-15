from django.contrib import admin

from .models import HealthData, Glucose, Pressure, Weight, PressureDataResult


class HealthAdmin(admin.ModelAdmin):
    list_display = ('userId', 'phonenumber', 'bloodsugar', 'bloodpressure', 'bloodcholesterol', 'bloodlevel', 'weight', 'created_at')

class GlucoseAdmin(admin.ModelAdmin):
    list_display = ('username', 'glucose', 'created_at')

class PressureAdmin(admin.ModelAdmin):
    list_display = ('username', 'systolic', 'diastolic', 'created_at')


class WeightAdmin(admin.ModelAdmin):
    list_display = ('username', 'weight', 'created_at')

class PressureDataResultAdmin(admin.ModelAdmin):
    list_display = ('created_at',)
    
admin.site.register(HealthData, HealthAdmin)
admin.site.register(Glucose, GlucoseAdmin)
admin.site.register(Pressure, PressureAdmin)
admin.site.register(Weight, WeightAdmin)
admin.site.register(PressureDataResult, PressureDataResultAdmin)
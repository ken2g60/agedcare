from django.contrib import admin

from .models import PersonalDetail , UserModel, ContributionModel

class UserAdmin(admin.ModelAdmin):
    list_display = ('userId', 'fullname', 'phonenumber', 'gender', 'balance', 'created_at')
    
    
    
class PersonalDetailAdmin(admin.ModelAdmin):
    list_display = ('user', 'date_of_birth','profile_userId', 'profile_phonenumber', 'height', 'occupation', 'sector', 'region', 'created_at')
    
    def profile_userId(self, x):
        return x.user.userId
    profile_userId.short_description = 'UserId'
    
    
    def profile_phonenumber(self, phone):
        return phone.user.phonenumber
    profile_phonenumber.short_description = 'PhoneNumber'


class ContributionModelAdmin(admin.ModelAdmin):
    list_display = ('userId', 'momo', 'amount', 'month', 'created_at')

admin.site.register(PersonalDetail, PersonalDetailAdmin)
admin.site.register(UserModel,  UserAdmin)
admin.site.register(ContributionModel, ContributionModelAdmin)
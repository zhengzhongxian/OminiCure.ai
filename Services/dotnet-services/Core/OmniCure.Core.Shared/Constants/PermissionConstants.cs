using OmniCure.Core.Shared.Helpers;

namespace OmniCure.Core.Shared.Constants;

public static class PermissionConstants
{
    public static class IAM
    {
        public const string View = "Permissions.IAM.View";
        public const string Create = "Permissions.IAM.Create";
        public const string Edit = "Permissions.IAM.Edit";
        public const string Delete = "Permissions.IAM.Delete";
    }

    public static class Patient
    {
        public const string View = "Permissions.Patient.View";
        public const string Create = "Permissions.Patient.Create";
        public const string Edit = "Permissions.Patient.Edit";
        public const string Delete = "Permissions.Patient.Delete";
    }

    public static List<string> GetAllPermissions()
    {
        return ReflectionHelper.GetConstants<string>(typeof(PermissionConstants));
    }
}

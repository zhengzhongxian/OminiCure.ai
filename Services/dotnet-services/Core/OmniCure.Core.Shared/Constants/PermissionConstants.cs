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

    public static class Appointment
    {
        public const string View = "Permissions.Appointment.View";
        public const string Create = "Permissions.Appointment.Create";
        public const string Edit = "Permissions.Appointment.Edit";
        public const string Delete = "Permissions.Appointment.Delete";
    }

    public static class Verification
    {
        public const string View = "Permissions.Verification.View";
        public const string Process = "Permissions.Verification.Process";
    }

    public static List<string> GetAllPermissions()
    {
        return ReflectionHelper.GetConstants<string>(typeof(PermissionConstants));
    }
}

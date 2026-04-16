package ai.omnicure.core.shared.constant;

public final class PermissionConstants {

    private PermissionConstants() {
        throw new UnsupportedOperationException("Constant class");
    }

    public static final class IAM {
        public static final String VIEW = "Permissions.IAM.View";
        public static final String CREATE = "Permissions.IAM.Create";
        public static final String UPDATE = "Permissions.IAM.Update";
        public static final String DELETE = "Permissions.IAM.Delete";
    }

    public static final class Patient {
        public static final String VIEW = "Permissions.Patient.View";
        public static final String CREATE = "Permissions.Patient.Create";
        public static final String UPDATE = "Permissions.Patient.Update";
        public static final String DELETE = "Permissions.Patient.Delete";
    }

    public static final class Appointment {
        public static final String VIEW = "Permissions.Appointment.View";
        public static final String CREATE = "Permissions.Appointment.Create";
        public static final String UPDATE = "Permissions.Appointment.Update";
        public static final String DELETE = "Permissions.Appointment.Delete";
    }

    public static final class Verification {
        public static final String VIEW = "Permissions.Verification.View";
        public static final String PROCESS = "Permissions.Verification.Process";
    }
}

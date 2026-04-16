namespace OmniCure.Core.Shared.Constants;

public static class KeyConstants
{
    public static class ConnectionStrings
    {
       
    }

    public static class ConfigurationSections
    {
        public const string PaginationSettings = "PaginationSettings";
    }

    public static class DatabaseInitializationSettings
    {
        public const string MaxRetries = "DatabaseInitializationSettings:MaxRetries";
        public const string RetryInterval = "DatabaseInitializationSettings:RetryInterval";
    }
}

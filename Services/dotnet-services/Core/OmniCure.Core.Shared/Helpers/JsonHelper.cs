using System.Text.Json;
using System.Text.Json.Serialization;

namespace OmniCure.Core.Shared.Helpers;

public static class JsonHelper
{
    private static readonly JsonSerializerOptions Options = new()
    {
        PropertyNameCaseInsensitive = true,
        PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
        Converters = { new JsonStringEnumConverter(JsonNamingPolicy.CamelCase) }
    };

    public static string ToJson<T>(this T value) => JsonSerializer.Serialize(value, Options);

    public static T? FromJson<T>(this string json) => JsonSerializer.Deserialize<T>(json, Options);
    
    public static List<T> ParseJsonArray<T>(this string? json, Func<JsonElement, (bool Success, T Value)> extractor)
    {
        if (string.IsNullOrWhiteSpace(json))
            return [];

        try
        {
            using var doc = JsonDocument.Parse(json);
            var results = new List<T>();
            
            foreach (var element in doc.RootElement.EnumerateArray())
            {
                var (success, value) = extractor(element);
                if (success)
                {
                    results.Add(value);
                }
            }
            
            return results.Distinct().ToList();
        }
        catch
        {
            return [];
        }
    }
    
    public static string? GetStringProperty(this JsonElement element, string propertyName)
    {
        if (element.TryGetProperty(propertyName, out var prop) ||
            element.TryGetProperty(char.ToLowerInvariant(propertyName[0]) + propertyName[1..], out prop))
        {
            return prop.GetString();
        }
        return null;
    }
}

using System.Reflection;

namespace OmniCure.Core.Shared.Helpers;

public static class ReflectionHelper
{
    public static List<TValue> GetConstants<TValue>(Type classType)
    {
        return GetConstantsRecursive<TValue>(classType).Distinct().ToList();
    }

    public static List<TFieldType> GetConstants<TClass, TFieldType>()
    {
        return GetConstants<TFieldType>(typeof(TClass));
    }

    public static Dictionary<string, object> GetAllConstants(Type classType)
    {
        return GetAllConstantsRecursive(classType, "");
    }

    private static List<TValue> GetConstantsRecursive<TValue>(Type classType)
    {
        var constants = classType
            .GetFields(BindingFlags.Public | BindingFlags.Static | BindingFlags.FlattenHierarchy)
            .Where(fi => fi is { IsLiteral: true, IsInitOnly: false } && fi.FieldType == typeof(TValue))
            .Select(fi => (TValue)fi.GetRawConstantValue()!)
            .ToList();
        
        foreach (var nestedType in classType.GetNestedTypes(BindingFlags.Public))
        {
            constants.AddRange(GetConstantsRecursive<TValue>(nestedType));
        }

        return constants;
    }

    private static Dictionary<string, object> GetAllConstantsRecursive(Type classType, string prefix)
    {
        var constants = classType
            .GetFields(BindingFlags.Public | BindingFlags.Static | BindingFlags.FlattenHierarchy)
            .Where(fi => fi is { IsLiteral: true, IsInitOnly: false })
            .ToDictionary(fi => prefix + fi.Name, fi => fi.GetRawConstantValue()!);
        
        foreach (var nestedType in classType.GetNestedTypes(BindingFlags.Public))
        {
            foreach (var kvp in GetAllConstantsRecursive(nestedType, prefix + nestedType.Name + "."))
            {
                constants.TryAdd(kvp.Key, kvp.Value);
            }
        }

        return constants;
    }
}

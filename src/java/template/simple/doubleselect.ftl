<#include "/${parameters.templateDir}/simple/select.ftl" />
<#assign startCount = 0/>
<#if parameters.headerKey?exists && parameters.headerValue?exists>
    <#assign startCount = startCount + 1/>
</#if>
<#if parameters.emptyOption?exists>
    <#assign startCount = startCount + 1/>
</#if>

<br/>
<select<#rt/>
 name="${parameters.doubleName?default("")?html}"<#rt/>
<#if parameters.disabled?default(false)>
 disabled="disabled"<#rt/>
</#if>
<#if parameters.doubleTabindex?exists>
 tabindex="${parameters.doubleTabindex?html}"<#rt/>
</#if>
<#if parameters.doubleId?exists>
 id="${parameters.doubleId?html}"<#rt/>
</#if>
<#if parameters.cssClass?exists>
 class="${parameters.cssClass?html}"<#rt/>
</#if>
<#if parameters.cssStyle?exists>
 style="${parameters.cssStyle?html}"<#rt/>
</#if>
<#if parameters.multiple?exists>
 multiple="multiple"<#rt/>
</#if>
>
</select>
<script type="text/javascript">
<#assign itemCount = startCount/>
    var ${parameters.name}Group = new Array(${parameters.listSize} + ${startCount});
    for (i = 0; i < (${parameters.listSize} + ${startCount}); i++)
    ${parameters.name}Group[i] = new Array();

<@ww.iterator value="parameters.list">
    <#if parameters.listKey?exists>
        <#assign itemKey = stack.findString(parameters.listKey)/>
    <#else>
        <#assign itemKey = stack.findString('top')/>
    </#if>
    <#if parameters.listValue?exists>
        <#assign itemValue = stack.findString(parameters.listValue)/>
    <#else>
        <#assign itemValue = stack.findString('top')/>
    </#if>
    <#assign doubleItems = stack.findValue(parameters.doubleList)/>
    <#assign doubleItemCount = 0/>
    <#if doubleItems?exists>
        <#list doubleItems as doubleItem>
            <#assign trash = stack.push(doubleItem)/>
            <#if parameters.doubleListKey?exists>
                <#assign doubleItemKey = stack.findValue(parameters.doubleListKey)/>
            <#else>
                <#assign doubleItemKey = doubleItem/>
            </#if>
            <#if parameters.doubleListValue?exists>
                <#assign doubleItemValue = stack.findValue(parameters.doubleListValue)/>
            <#else>
                <#assign doubleItemValue = doubleItem/>
            </#if>
    ${parameters.name}Group[${itemCount}][${doubleItemCount}] = new Option("${doubleItemKey}", "${doubleItemValue}");
            <#assign doubleItemCount = doubleItemCount + 1/>
            <#assign trash = stack.pop()/>
        </#list>
        <#assign itemCount = itemCount + 1/>
    </#if>
</@ww.iterator>

    var ${parameters.name}Temp = document.${parameters.formName}.${parameters.doubleName};
<#assign itemCount = startCount/>
<#assign redirectTo = 0/>
<@ww.iterator value="parameters.list">
    <#if parameters.listValue?exists>
        <#assign itemValue = stack.findString(parameters.listValue)/>
    <#else>
        <#assign itemValue = stack.findString('top')/>
    </#if>
    <#if tag.contains(parameters.nameValue, itemKey)>
        <#assign redirectTo = itemCount/>
    </#if>
    <#assign itemCount = itemCount + 1/>
</@ww.iterator>
    ${parameters.name}Redirect(${redirectTo});

    function ${parameters.name}Redirect(x) {
        for (m = ${parameters.name}Temp.options.length - 1; m >= 0; m--) {
            ${parameters.name}Temp.options[m] = null;
        }

        for (i = 0; i < ${parameters.name}Group[x].length; i++) {
            ${parameters.name}Temp.options[i] = new Option(${parameters.name}Group[x][i].text, ${parameters.name}Group[x][i].value);
        }

        if (${parameters.name}Temp.options.length > 0) {
            ${parameters.name}Temp.options[0].selected = true;
        }
    }
</script>
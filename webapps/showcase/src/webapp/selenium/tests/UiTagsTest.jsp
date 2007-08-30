<%--
  @author tmjee
  @version $Date$ $Id$
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="ww" uri="/webwork" %>
<html>
  <head>
      <title>UiTagsTest</title>
      <meta content="plain" name="decorator" />
  </head>
  <body>
    <table>
        <tr>
            <td colspan="3">UiTagTest</td>
        </tr>
        <tr>
            <td>open</td>
            <td><ww:url namespace="/tags/ui" action="example" method="input" includeContext="true" includeParams="none"/> </td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Name</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Birthday</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Biography</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Favourite Color</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Favourite Language</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Friends</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>State</td>
            <td></td>
        </tr>
        <tr>
           <td>verifyTextPresent</td>
           <td>Favourite Vehical</td>
           <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Picture</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Favourite Cartoons Characters</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Your Thoughts</td>
            <td></td>
        </tr>
        <tr>
            <td>type</td>
            <td>exampleSubmit_name</td>
            <td>Toby</td>
        </tr>
        <tr>
            <td>type</td>
            <td>exampleSubmit_birthday</td>
            <td>08/11/2007</td>
        </tr>
        <tr>
            <td>type</td>
            <td>exampleSubmit_bio</td>
            <td>I am a Java Programmer</td>
        </tr>
        <tr>
            <td>select</td>
            <td>exampleSubmit_favoriteColor</td>
            <td>value=Blue</td>
        </tr>
        <tr>
            <td>select</td>
            <td>exampleSubmit_favouriteLanguage</td>
            <td>value=FrenchKey</td>
        </tr>
        <tr>
            <td>click</td>
            <td>friends-2</td>
            <td></td>
        </tr>
        <tr>
            <td>click</td>
            <td>friends-3</td>
            <td></td>
        </tr>
        <tr>
            <td>click</td>
            <td>exampleSubmit_legalAge</td>
            <td></td>
        </tr>
        <tr>
            <td>select</td>
            <td>exampleSubmit_region</td>
            <td>value=North</td>
        </tr>
        <tr>
            <td>select</td>
            <td>exampleSubmit_state</td>
            <td>value=Washington</td>
        </tr>
        <tr>
            <td>select</td>
            <td>exampleSubmit_favouriteVehicalType</td>
            <td>value=CarKey</td>
        </tr>
        <tr>
            <td>select</td>
            <td>exampleSubmit_favouriteVehicalSpecific</td>
            <td>value=FordKey</td>
        </tr>
        <tr>
            <td>clickAndWait</td>
            <td>exampleSubmit_0</td>
            <td></td>
        </tr>
        <tr>
            <td>storeAlert</td>
            <td>alertVar</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyExpression</td>
            <td>${alertVar}</td>
            <td>You are submiting this form.</td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Toby</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>8/11/07</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>I am a Java Programmer</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>[Jason, Jay]</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>true</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>North</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>Washington</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>FrenchKey</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>CarKey</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>FordKey</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>1.Popeye<td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>2.He-Man</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>3.Spiderman</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>1.Superman</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>2.Mickey Mouse</td>
            <td></td>
        </tr>
        <tr>
            <td>verifyTextPresent</td>
            <td>3.Donald Duck </td>
            <td></td>
        </tr>
    </table>
  </body>
</html>
<%@ attribute name="balanceSheet" type="br.com.camiloporto.cloudfinance.service.impl.BalanceSheet" required="true"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cf" tagdir="/WEB-INF/tags/cloudfinance" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<cf:balanceSheetTree topNode="${balanceSheet.assetBalanceSheetTree}"></cf:balanceSheetTree>
<cf:balanceSheetTree topNode="${balanceSheet.liabilityBalanceSheetTree}"> </cf:balanceSheetTree>

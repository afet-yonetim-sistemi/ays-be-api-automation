package utility;

import org.testng.annotations.DataProvider;

import java.io.IOException;

public class ExcelDataProvider {
    private XLUtility xl;

    public ExcelDataProvider() throws IOException {
        String path = System.getProperty("user.dir") + "pathOfExcelFile";
        xl = new XLUtility(path);
    }

    @DataProvider(name = "Data")
    public Object[][] getAllData() throws IOException {
        int rowNum = xl.getRowCount("Sheet1");
        int colCount = xl.getCellCount("Sheet1", 1);

        Object[][] apiData = new String[rowNum][colCount];

        for (int i = 1; i <= rowNum; i++) {
            for (int j = 0; j < colCount; j++) {
                apiData[i - 1][j] = xl.getCellData("Sheet1", i, j);
            }
        }
        return apiData;
    }

    @DataProvider(name = "UserNames")
    public Object[][] getUserNames() throws IOException {
        int rowNum = xl.getRowCount("Sheet1");

        Object[][] apiData = new Object[rowNum][1];
        for (int i = 1; i <= rowNum; i++) {
            apiData[i - 1][0] = xl.getCellData("Sheet1", i, 1);
        }
        return apiData;
    }

}

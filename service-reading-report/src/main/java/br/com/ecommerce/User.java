package br.com.ecommerce;

public record User(String uuid) {

    public String getReportPath() {
        return "target/" + uuid + "-report.txt";
    }

}

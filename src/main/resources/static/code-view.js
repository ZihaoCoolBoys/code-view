
loadLanguageCount();
loadMethodCount();
loadSourceCreateNum();

function loadLanguageCount(){
	
	var chartLanguage = echarts.init(document.getElementById('languageCount'));
	
    var languageCount = {
            title: {
                text: '简要信息统计'
            },
            tooltip: {},
            legend: {
                data:['数据量']
            },
            xAxis: {
                data: ["源文件数","if","for","while"]
            },
            yAxis: {},
            series: [{
                name: '数据量',
                type: 'bar',
                data: ${languageCount}
            }]
    };

    chartLanguage.setOption(languageCount);
}

function loadMethodCount() {

	var chartMethod = echarts.init(document.getElementById('methodCount'));
	
    var methodCount = {
            title: {
                text: '代码信息统计'
            },
            tooltip: {},
            legend: {
                data:['数据量']
            },
            xAxis: {
                data: ["函数个数","最长函数行","平均函数行"]
            },
            yAxis: {},
            series: [{
                name: '数据量',
                type: 'bar',
                data: ${methodCount}
            }]
    };

    chartMethod.setOption(methodCount);
}

function loadSourceCreateNum() {

    var chartMethod = echarts.init(document.getElementById('sourceCreateNum'));
    var data = ${sourceCreateNum};

    var dateList = data.map(function (item) {
        return item[0];
    });
    var valueList = data.map(function (item) {
        return item[1];
    });

    var option = {

        visualMap: [{
            show: false,
            type: 'continuous',
            seriesIndex: 0,
            min: 0,
            max: 400
        }],


        title: [{
            left: 'center',
            text: '日期工程源码数量'
        }],
        tooltip: {
            trigger: 'axis'
        },
        xAxis: [{
            data: dateList
        }],
        yAxis: [{
            splitLine: {show: false}
        }],
        grid: [{
            bottom: '60%'
        }],
        series: [{
            type: 'line',
            showSymbol: false,
            data: valueList
        }]
    };

    chartMethod.setOption(option);
}
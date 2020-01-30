import csv

import matplotlib.pyplot as plt


def generate(data):
    staticFullSummary = data['staticFullSummary']
    dynamicFullSummary = data['dynamicFullSummary']
    name = staticFullSummary['type'].lower()
    title = name.capitalize()
    testedParameterValues = getFirstValueFromList(staticFullSummary['summaries'], 'testedParameterValues')

    # success full access
    sSuccessFullAccessValues = getValues(staticFullSummary['summaries'], 'successFullAccess')
    dSuccessFullAccessValues = getValues(dynamicFullSummary['summaries'], 'successFullAccess')

    plt.clf()
    plt.title(title)
    plt.plot(testedParameterValues, sSuccessFullAccessValues, 'o-', testedParameterValues, dSuccessFullAccessValues, 'o-')
    plt.xlabel(getTestedParameterName(staticFullSummary['testedParameter']).split(';')[0])
    plt.ylabel('Success full access[%]')
    plt.legend(['Static', 'Dynamic'])
    plt.savefig('../generated/' + name + '_successFullAccess.png')

    # average access time
    sAverageAccessTimeValues = getValues(staticFullSummary['summaries'], 'averageAccessTime')
    dAverageAccessTimeValues = getValues(dynamicFullSummary['summaries'], 'averageAccessTime')

    plt.clf()
    plt.title(title)
    plt.plot(testedParameterValues, sAverageAccessTimeValues, 'o-', testedParameterValues, dAverageAccessTimeValues, 'o-')
    plt.xlabel(getTestedParameterName(staticFullSummary['testedParameter']).split(';')[0])
    plt.ylabel('Average access time [s]')
    plt.legend(['Static', 'Dynamic'])
    plt.savefig('../generated/' + name + '_averageAccessTime.png')

    writeToCSV(staticFullSummary, dynamicFullSummary, name)


def getValues(summaries, name):
    list = []
    for item in summaries:
        list.append(item[name])
    return list

def getFirstValueFromList(summaries, name):
    list = []
    for item in summaries:
        list.append(item[name][0])
    return list


def writeToCSV(staticFullSummary, dynamicFullSummary, name):
    staticSummary = staticFullSummary['summaries']
    dynamicSummary = dynamicFullSummary['summaries']
    testedParamter = staticFullSummary['testedParameter']
    f = open('../generated/' + name + '_summary.csv', 'w', newline='\n', encoding='utf-8')
    with f:
        lp = 'lp.'
        tp = getTestedParameterName(testedParamter)
        ssa = name.capitalize() + ' (static) success full available [%]'
        sat = name.capitalize() + ' (static) average access time [s]'
        dsa = name.capitalize() + '(dynamic) success full available [%]'
        dat = name.capitalize() + '(dynamic) average access time [s]'
        if testedParamter == 'AVAILABILITY_DECREASING' or testedParamter == 'AVAILABILITY_INCREASING':
            tp = tp.split(';')
            tp1 = tp[0]
            tp2 = tp[1]
            fnames = [lp, tp1, tp2, ssa, sat, dsa, dat]
            writer = csv.DictWriter(f, fieldnames=fnames)
            writer.writeheader()
            for idx in range(len(staticSummary)):
                values = staticSummary[idx]['testedParameterValues']
                value1 = values[0]
                value2 = values[1]
                writer.writerow({
                    lp: idx + 1,
                    tp1: value1,
                    tp2: value2,
                    ssa: round(staticSummary[idx]['successFullAccess'], 2),
                    sat: staticSummary[idx]['averageAccessTime'],
                    dsa: round(dynamicSummary[idx]['successFullAccess'], 2),
                    dat: dynamicSummary[idx]['averageAccessTime'],
                })
        else:
            fnames = [lp, tp, ssa, sat, dsa, dat]
            writer = csv.DictWriter(f, fieldnames=fnames)
            writer.writeheader()
            for idx in range(len(staticSummary)):
                writer.writerow({
                    lp: idx + 1,
                    tp: staticSummary[idx]['testedParameterValues'][0],
                    ssa: round(staticSummary[idx]['successFullAccess'], 2),
                    sat: staticSummary[idx]['averageAccessTime'],
                    dsa: round(dynamicSummary[idx]['successFullAccess'], 2),
                    dat: dynamicSummary[idx]['averageAccessTime'],
                })


def getTestedParameterName(testedParameter):
    switcher = {
        'NONE': 'None',
        'REPLICA': 'Number of replicas',
        'NODE': 'Number of nodes',
        'AVAILABILITY_DECREASING': 'Availability - weak nodes;Availability - strong nodes',
        'AVAILABILITY_INCREASING': 'Availability - weak nodes;Availability - strong nodes',
    }
    return switcher.get(testedParameter, "None")

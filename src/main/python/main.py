import math

import numpy as np
import matplotlib.pyplot as plt


def generate():
    with open("statsAll.txt") as file_name:
        array = np.genfromtxt(file_name, dtype=None, encoding="UTF-8", delimiter=",")
    length = len(array)

    # Cols
    # 'moves', 'board_number', 'strategy', 'type_of_strategy', 'number_of_visited_states',
    # 'number_of_processed_states', 'max_recursion_depth', 'duration'

    criteria = ['długość znalezionego rozwiązania', 'liczba stanów odwiedzonych', 'liczba stanów przetworzonych',
                'maksymalna osiągnięta głębokość rekursji', 'czas trwania procesu obliczeniowego']

    strategies = ['bfs', 'dfs', 'astr']
    strategiesTitles = ['BFS', 'DFS', 'A*']

    typesOfStrategies = {
        "bfs": ['rdul', 'rdlu', 'drul', 'drlu', 'ludr', 'lurd', 'uldr', 'ulrd'],
        "dfs": ['rdul', 'rdlu', 'drul', 'drlu', 'ludr', 'lurd', 'uldr', 'ulrd'],
        "astr": ['hamm', 'manh'],
    }
    typesOfStrategiesTitles = {
        "bfs": ['RDUL', 'RDLU', 'DRUL', 'DRLU', 'LUDR', 'LURD', 'ULDR', 'ULRD'],
        "dfs": ['RDUL', 'RDLU', 'DRUL', 'DRLU', 'LUDR', 'LURD', 'ULDR', 'ULRD'],
        "astr": ['Hamming', 'Manhattan'],
    }

    moves = [1, 2, 3, 4, 5, 6, 7]
    x = np.arange(len(moves))

    for i in range(0, len(criteria)):
        plt.figure(figsize=(12.8, 9.6))

        plt.subplot(2, 2, 1)
        plt.subplots_adjust(bottom=0.06, left=0.05, right=0.99, top=0.95)
        plt.rcParams.update({'font.size': 12})
        # resultsTotal[strategy][move]
        resultsTotal = []
        for j in range(0, len(strategies)):
            movesResults = []
            for k in range(0, len(moves)):
                newArray = []
                for l in range(0, length):
                    record = array[l]
                    if record[0] == moves[k] and record[2] == strategies[j]:
                        newArray.append(record[4 + i])
                if len(newArray) == 0:
                    movesResults.append(0)
                else:
                    movesResults.append(sum(newArray) / len(newArray))
            resultsTotal.append(movesResults)

        width = 0.8 / len(strategies)
        first = x - (width * math.floor(len(strategies) / 2))
        for k in range(0, len(strategies)):
            plt.bar(first + k * width, resultsTotal[k], width, label=strategiesTitles[k])
        plt.ylabel(criteria[i])
        plt.title('Ogółem', fontsize=20)
        plt.xticks(x, moves)
        plt.legend()

        plt.subplot(2, 2, 2)
        # resultsAstr[typeOfStrategy][move]
        resultsAstr = []
        for j in range(0, len(typesOfStrategies[strategies[2]])):
            movesResults = []
            for k in range(0, len(moves)):
                newArray = []
                for l in range(0, length):
                    record = array[l]
                    if record[0] == moves[k] and record[2] == strategies[2] and record[3] == \
                            typesOfStrategies[strategies[2]][j]:
                        newArray.append(record[4 + i])
                if len(newArray) == 0:
                    movesResults.append(0)
                else:
                    movesResults.append(sum(newArray) / len(newArray))
            resultsAstr.append(movesResults)

        width = 0.8 / len(typesOfStrategies[strategies[2]])
        first = x - (width / 2)
        for k in range(0, len(typesOfStrategies[strategies[2]])):
            plt.bar(first + k * width, resultsAstr[k], width, label=typesOfStrategiesTitles[strategies[2]][k])
        plt.title(strategiesTitles[2], fontsize=20)
        plt.xticks(x, moves)
        plt.legend()

        plt.subplot(2, 2, 3)
        # resultsBfs[typeOfStrategy][move]
        resultsBfs = []
        for j in range(0, len(typesOfStrategies[strategies[0]])):
            movesResults = []
            for k in range(0, len(moves)):
                newArray = []
                for l in range(0, length):
                    record = array[l]
                    if record[0] == moves[k] and record[2] == strategies[0] and record[3] == \
                            typesOfStrategies[strategies[0]][j]:
                        newArray.append(record[4 + i])
                if len(newArray) == 0:
                    movesResults.append(0)
                else:
                    movesResults.append(sum(newArray) / len(newArray))
            resultsBfs.append(movesResults)

        width = 0.8 / len(typesOfStrategies[strategies[0]])
        first = x - (width * math.floor(len(typesOfStrategies[strategies[0]]) / 2))
        for k in range(0, len(typesOfStrategies[strategies[0]])):
            plt.bar(first + k * width, resultsBfs[k], width, label=typesOfStrategiesTitles[strategies[0]][k])
        plt.title(strategiesTitles[0], fontsize=20)
        plt.xticks(x, moves)
        plt.ylabel(criteria[i])
        plt.xlabel('Głębokość')
        plt.legend(ncol=2)

        plt.subplot(2, 2, 4)
        # resultsDfs[typeOfStrategy][move]
        resultsDfs = []
        for j in range(0, len(typesOfStrategies[strategies[1]])):
            movesResults = []
            for k in range(0, len(moves)):
                newArray = []
                for l in range(0, length):
                    record = array[l]
                    if record[0] == moves[k] and record[2] == strategies[1] and record[3] == \
                            typesOfStrategies[strategies[1]][j]:
                        newArray.append(record[4 + i])
                if len(newArray) == 0:
                    movesResults.append(0)
                else:
                    movesResults.append(sum(newArray) / len(newArray))
            resultsDfs.append(movesResults)

        width = 0.8 / len(typesOfStrategies[strategies[1]])
        first = x - (width * math.floor(len(typesOfStrategies[strategies[1]]) / 2))
        for k in range(0, len(typesOfStrategies[strategies[1]])):
            plt.bar(first + k * width, resultsDfs[k], width, label=typesOfStrategiesTitles[strategies[1]][k])
        plt.title(strategiesTitles[1], fontsize=20)
        plt.xticks(x, moves)
        plt.xlabel('Głębokość')
        plt.show()


if __name__ == '__main__':
    generate()

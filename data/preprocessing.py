import csv
import math

FILE = "poker-hand-training-true.csv"
NORM_FILE = "poker-hand-normalized.csv"
HEART = (0, 0)
SPADE = (0, 1)
DIAMOND = (1, 0)
CLUB = (1, 1)


def compute_sum_of_values():
    with open(FILE) as file:
        reader = csv.reader(file, delimiter=",")
        sum1, sum2, sum3, sum4, sum5 = 0, 0, 0, 0, 0
        count_of_lines = 0

        for line in reader:
            if len(line) != 11:
                print("Error! Less than 11 attributes.")
                return
            sum1 += int(line[1])
            sum2 += int(line[3])
            sum3 += int(line[5])
            sum4 += int(line[7])
            sum5 += int(line[9])
            count_of_lines += 1

            if count_of_lines % 10000 == 0:
                print(str(count_of_lines/10000) + "0k was procesed")

    return sum1,sum2,sum3,sum4,sum5, count_of_lines


def compute_sum_for_deviation(mean1, mean2, mean3, mean4, mean5):
    with open(FILE) as file:
        reader = csv.reader(file, delimiter=",")

        sum1, sum2, sum3, sum4, sum5 = 0, 0, 0, 0, 0
        for line in reader:
            if len(line) != 11:
                print("Error! Less than 11 attributes.")
                return
            sum1 += (int(line[1]) - mean1) ** 2
            sum2 += (int(line[3]) - mean2) ** 2
            sum3 += (int(line[5]) - mean3) ** 2
            sum4 += (int(line[7]) - mean4) ** 2
            sum5 += (int(line[9]) - mean5) ** 2

    return math.sqrt(sum1),math.sqrt(sum2),math.sqrt(sum3),math.sqrt(sum4),math.sqrt(sum5)


def normalize_data(stand_deviations, means):
    f = open(NORM_FILE, "w")
    with open(FILE) as file:
        reader = csv.reader(file, delimiter=",")

        for line in reader:
            if len(line) != 11:
                print("Error! Less than 11 attributes.")
                return
            for i in [0,2,4,6,8]:
                if line[i] == "1":
                    line[i] = HEART
                if line[i] == "2":
                    line[i] = SPADE
                if line[i] == "3":
                    line[i] = DIAMOND
                if line[i] == "4":
                    line[i] = CLUB
            for i in [1,3,5,7,9]:
                # // 2, lebo 1 // 2 == 0, 3 // 2 == 1, 5 // 2 == 2
                # co odpoveda [1,3,5,7,9] -> [0,1,2,3,4] (stlpce v tab. -> pole odchylok)
                line[i] = (int(line[i]) - means[i // 2]) / stand_deviations[i // 2]
            f.write(str(line) + "\n")

    f.close()


if __name__ == '__main__':
    sum1,sum2,sum3,sum4,sum5, count_of_lines = compute_sum_of_values()
    mean1 = sum1 / count_of_lines
    mean2 = sum2 / count_of_lines
    mean3 = sum3 / count_of_lines
    mean4 = sum4 / count_of_lines
    mean5 = sum5 / count_of_lines

    stand_dev1, stand_dev2, stand_dev3, stand_dev4, stand_dev5 = compute_sum_for_deviation(mean1, mean2, mean3, mean4, mean5)
    normalize_data([stand_dev1, stand_dev2, stand_dev3, stand_dev4, stand_dev5], [mean1, mean2, mean3, mean4, mean5])



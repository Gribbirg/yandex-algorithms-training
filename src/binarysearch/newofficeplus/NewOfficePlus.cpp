#include <iostream>
#include <fstream>
#include <string>
#include <vector>

using namespace std;

bool checkSquare(const vector<vector<int>>& sums, int startLine, int startChar, int sizeLine, int sizeChar) {
    return sums[startLine][startChar] +
           sums[startLine + sizeLine][startChar + sizeChar] -
           sums[startLine][startChar + sizeChar] -
           sums[startLine + sizeLine][startChar] == sizeLine * sizeChar;
}

bool checkPlus(const vector<vector<int>>& sums, int startLine, int startChar, int size) {
    return startLine - size >= 0 && startLine + size * 2 < sums.size() &&
           startChar - size >= 0 && startChar + size * 2 < sums[0].size() &&
           checkSquare(sums, startLine, startChar - size, size, size * 3) &&
           checkSquare(sums, startLine - size, startChar, size * 3, size);
}

int main() {
    ifstream input("input.txt");
    string firstLine;
    getline(input, firstLine);
    int height = stoi(firstLine.substr(0, firstLine.find(' ')));
    int width = stoi(firstLine.substr(firstLine.find(' ')));

    vector<string> field;

    for (int i = 0; i < height; ++i) {
        getline(input, firstLine);
        field.push_back(firstLine);
    }

    vector<vector<int>> sums;
    for (int i = 0; i <= height; ++i) {
        sums.emplace_back();
        sums[i].push_back(0);
    }
    for (int i = 1; i <= width; i++) {
        sums[0].push_back(0);
    }
    for (int i = 1; i <= height; ++i) {
        for (int j = 1; j <= width; ++j) {
            sums[i].push_back(
                    sums[i][j - 1] + sums[i - 1][j] - sums[i - 1][j - 1] + ((field[i - 1][j - 1] == '#') ? 1 : 0));
        }
    }

    auto maxSize = min(width, height) / 3;

    auto currentSize = 1;
    auto lineIndex = 1;
    while (lineIndex <= height) {
        auto charIndex = currentSize;
        while (charIndex <= width) {
            if (checkPlus(sums, lineIndex, charIndex, currentSize)) {
                auto l = currentSize;
                auto r = maxSize;
                while (l < r) {
                    auto medium = (l + r + 1) / 2;
                    if (checkPlus(sums, lineIndex, charIndex, medium)) {
                        l = medium;
                    } else {
                        r = medium - 1;
                    }
                }
                currentSize = l + 1;
            }
            charIndex++;
        }
        lineIndex++;
    }

    cout << currentSize - 1;
    return 0;
}


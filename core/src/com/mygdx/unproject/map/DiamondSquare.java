package com.mygdx.unproject.map;

/*
This algorithm disregards any metric
 */
public class DiamondSquare extends GeneratorDiamondSquareHeightMap {
    private SplittingLine splitLine;
    private int count;
    private double sum;
    public DiamondSquare(int seed,SplittingLine splitLine) {
        super(seed);
        this.splitLine = splitLine;
    }

    public void generate(double[][] map, double roughness) {
        initialCornerValues(map,roughness);
        for(int step = (map.length >> 1); step > 0; step >>= 1){
            performDiamondStep(map,roughness,step);
            performSquareStep(map,roughness,step);
        }
        avarage = sum / count;
    }
    private void initialCornerValues(double[][] map, double roughness){
        double randomValue = random.nextDouble()*2*roughness - roughness;
        map[0][0] = calculateValue(randomValue);

        randomValue = random.nextDouble()*2*roughness - roughness;
        map[map.length - 1][map[0].length - 1] = calculateValue(randomValue);

        randomValue = random.nextDouble()*2*roughness - roughness;
        map[0][map[0].length - 1] = calculateValue(randomValue);

        randomValue = random.nextDouble()*2*roughness - roughness;
        map[map.length - 1][0] = calculateValue(randomValue);
    }
    private void performDiamondStep(double[][] map, double roughness, int step){
        for(int y = step; y < map.length; y += 2 * step){
            for(int x = step; x < map[0].length; x += 2 * step){
                double avarage = map[y - step][x - step] + map[y + step][x + step];
                avarage += map[y + step][x - step] + map[y - step][x + step];
                avarage /= 4;
                double l = splitLine.getSplitting(x - step,y-step,x + step, y + step);
                double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
                double result = avarage + randomValue;

               map[y][x] = calculateValue(result);
            }
        }
    }
    private void performSquareStep(double[][] map, double roughness, int step){
        double l = splitLine.getSplitting(0,0,0,2 * step);
        int y = 0;
        for(int x = step; x < map[0].length; x += 2*step){
            double avarage = map[y][x - step] + map[y][x + step];
            avarage += map[y + step][x];
            avarage /= 3;
            double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
            double result = avarage + randomValue;

            map[y][x] = calculateValue(result);
        }
        y = map.length - 1;
        for(int x = step; x < map[0].length; x += 2*step){
            double avarage = map[y][x - step] + map[y][x + step];
            avarage += map[y - step][x];
            avarage /= 3;
            double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
            double result = avarage + randomValue;

            map[y][x] = calculateValue(result);
        }

        int x = 0;
        for(y = step; y < map.length; y += 2*step){
            double avarage = map[y - step][x] + map[y + step][x];
            avarage += map[y][x + step];
            avarage /= 3;
            double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
            double result = avarage + randomValue;

            map[y][x] = calculateValue(result);
        }
        x = map[0].length - 1;
        for(y = step; y < map.length; y += 2*step){
            double avarage = map[y - step][x] + map[y + step][x];
            avarage += map[y][x - step];
            avarage /= 3;
            double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
            double result = avarage + randomValue;

            map[y][x] = calculateValue(result);
        }
        for(y = step; y < map.length - 1; y += 2 * step){
            for(x = 2 * step; x < map[0].length - 1; x += 2 * step){
                double avarage = map[y][x - step] + map[y][x + step];
                avarage += map[y + step][x] + map[y - step][x];
                avarage /= 4;
                double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
                double result = avarage + randomValue;
                map[y][x] = calculateValue(result);
            }
        }
        for(y = 2 * step; y < map.length - 1; y += 2 * step){
            for(x = step; x < map[0].length - 1; x += 2 * step){
                double avarage = map[y][x - step] + map[y][x + step];
                avarage += map[y + step][x] + map[y - step][x];
                avarage /= 4;
                double randomValue = random.nextDouble()*2*roughness * l - roughness * l;
                double result = avarage + randomValue;
                map[y][x] = calculateValue(result);
            }
        }
    }
    private double calculateValue(double result){
        double value = result;
        if(value < minHeight){
            minHeight = value;
        }else if(value > maxHeight){
            maxHeight = value;
        }
        sum += value;
        count++;
        return value;
    }
    /*
    public static void main(String[] args) {
        DiamondSquare d = new DiamondSquare(2, new SplittingLine() {
            public double getSplitting(int x1, int y1, int x2, int y2) {
                return y2 > y1 ? (y2 - y1)>>2 : (x2 - x1)>>2;
            }
        });
        double map[][] = new double[9][9];
        d.generate(map,0.5);
    }*/
}

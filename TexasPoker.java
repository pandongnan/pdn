import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TexasPoker {
    private String cards;
    private int[] cardsNumber;  //记录手牌中所有的点数
    private char[] cardsColor;  //记录手牌中所有的花色
    private Map<Integer, Integer> numberInfo;       //记录手牌中所有点数出现的次数
    private Map<Character, Integer> colorInfo;      //记录手牌中所有花色出现的次数

    //将'2'到'A'的每个点数映射成int,方便比较大小
    private static Map<Character, Integer> numberMapper = new HashMap<Character, Integer>(){{
        put('2', 2);
        put('3', 3);
        put('4', 4);
        put('5', 5);
        put('6', 6);
        put('7', 7);
        put('8', 8);
        put('9', 9);
        put('T', 10);
        put('J', 11);
        put('Q', 12);
        put('K', 13);
        put('A', 14);
    }};

    public TexasPoker(){}
    public TexasPoker(String cards){
        this.cards = cards;
        getNumberAndColor();
        getNumberInfo();
        getColorInfo();
    }

    //是否同花顺
    public boolean isStraightFlush(){
        if(colorInfo.size() > 1)    return false;
        if(numberInfo.size() < cardsNumber.length)  return false;
        int[] numbers = new int[cardsNumber.length];
        numbers = cardsNumber.clone();
        Arrays.sort(numbers);
        if(numbers[numbers.length - 1] - numbers[0] != numbers.length - 1)
            return false;
        else
            return true;
    }

    //是否同花
    public boolean isFlush(){
        if(colorInfo.size() > 1)    return false;
        return !isStraightFlush();
    }

    //是否顺子
    public boolean isStraight(){
        if(colorInfo.size() == 1)   return false;
        if(numberInfo.size() < cardsNumber.length)  return false;
        int[] numbers = new int[cardsNumber.length];
        numbers = cardsNumber.clone();
        Arrays.sort(numbers);
        if(numbers[numbers.length - 1] - numbers[0] == numbers.length - 1)
            return true;
        else
            return false;
    }

    //是否三条
    public boolean isThreeOfAKind(){
        for(Integer key : numberInfo.keySet()){
            if(numberInfo.get(key) >= 3)    return true;
        }
        return false;
    }

    //是否两对
    public boolean isTwoPairs(){
        int pairs = 0;
        for(Integer key : numberInfo.keySet()){
            if(numberInfo.get(key) == 4)
                return true;
            if(numberInfo.get(key) == 2)
                ++pairs;
        }

        return pairs == 2;
    }

    //是否一对
    public boolean isOnePair(){
        int pairs = 0;
        for(Integer key : numberInfo.keySet())
            if(numberInfo.get(key) == 2)
                ++pairs;
        return pairs == 1;
    }

    //是否散牌
    public boolean isOffsuit(){
        return !(isStraightFlush() | isStraight() | isFlush() |
                isOnePair() | isThreeOfAKind() | isTwoPairs());
    }

    //返回手牌组合的类型，返回最“厉害”的牌型
    public String score(){
        if(isStraightFlush())   return "StraightFlush";
        if(isFlush())    return "Flush";
        if(isStraight())    return "Straight";
        if(isThreeOfAKind())    return "ThreeOfAKind";
        if(isTwoPairs())    return "TwoPairs";
        if(isOnePair())   return "OnePair";
        return "Offsuit";
    }

    //计算手牌中包含的点数和花色
    private void getNumberAndColor(){
        String[] cds = this.cards.split(" ");
        this.cardsNumber = new int[cds.length];
        this.cardsColor = new char[cds.length];
        for(int i = 0;i < cds.length;++i) {
            char number = cds[i].charAt(0);
            char color = cds[i].charAt(1);
            this.cardsNumber[i] = numberMapper.get(number);
            this.cardsColor[i] = color;
        }
    }

    //计算手牌中各点数出现的次数
    private void getNumberInfo(){
        this.numberInfo = new HashMap<>();
        for(int i = 0;i < cardsNumber.length;++i){
            int num = cardsNumber[i];
            numberInfo.put(num, !numberInfo.containsKey(num) ? 1 : numberInfo.get(num) + 1);
        }
    }

    //计算手牌中各花色出现的次数
    private void getColorInfo(){
        this.colorInfo = new HashMap<>();
        for(int i = 0;i < cardsColor.length;++i){
            char color = cardsColor[i];
            colorInfo.put(color, !colorInfo.containsKey(color) ? 1 : colorInfo.get(color) + 1);
        }
    }

    public void setCards(String cards){
        this.cards = cards;
        getNumberAndColor();
        getNumberInfo();
        getColorInfo();
    }

    public int getCardsLength(){
        return cardsNumber.length;
    }

    //返回手牌中第N大的点数
    public int getNthMaxNumber(int Nth){
        int[] numbers = new int[cardsNumber.length];
        numbers = cardsNumber.clone();
        Arrays.sort(numbers);
        return numbers[numbers.length - Nth];
    }

    //返回手牌中组成三条的点数
    public int getThreeOfAKindNumber(){
        for(Integer key : numberInfo.keySet()){
            if(numberInfo.get(key) == 3)
                return key;
        }
        return -1;
    }

    //以数组的形式返回手牌中组成对子和落单的点数,数组中前两项为组成手牌中两个对子的点数,最后一项为手牌中落单的点数
    public int[] getTwoPairsNumber(){
        int[] numbers = new int[2 + cardsNumber.length - 4];
        int pair = 0, single = 2;
        for(Integer key : numberInfo.keySet()){
            if(numberInfo.get(key) == 4){
                numbers[pair++] = key;
                numbers[pair++] = key;
            }
            else if(numberInfo.get(key) == 2)    numbers[pair++] = key;
            else if(numberInfo.get(key) == 1)   numbers[single++] = key;
        }
        //点数大的对子在前
        if(numbers[0] < numbers[1]){
            int tmp = numbers[0];
            numbers[0] = numbers[1];
            numbers[1] = tmp;
        }
        return numbers;
    }

    //以数组的形式返回手牌中组成对子的落单的点数,数组中第一项为组成对子的点数,之后为落单的点数
    public int[] getOnePairNumber(){
        int[] numbers = new int[1 + cardsNumber.length - 2];
        int pair = 0, single = 1;
        for(Integer key : numberInfo.keySet()){
            if(numberInfo.get(key) == 2)    numbers[pair] = key;
            else if(numberInfo.get(key) == 1)   numbers[single++] = key;
        }
        return numbers;
    }
}

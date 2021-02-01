package ru.pilot.pathwork.name;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NamingFactory {
    private static final ArrayList<String> NAME_1 = new ArrayList<>(Arrays.asList("солнечный", "плюшевый" ,"бешеный" ,"памятный" ,"трепетный" ,"базовый" ,"преданный" ,"ласковый" ,"пойманный" ,"радужный" ,"огненный" ,"радостный" ,"шёлковый" ,"пепельный" ,"жареный"));
    private static final ArrayList<String> NAME_2 = new ArrayList<>(Arrays.asList("зайчик" , "пейчворк", "Верник" ,"глобус" ,"ветер" ,"щавель" ,"песик" ,"копчик" ,"стольник" ,"мальчик" ,"дольщик" ,"Игорь" ,"невод" ,"егерь" ,"лобстер" ,"жемчуг" ,"йогурт"));
    private static final ArrayList<String> NAME_3 = new ArrayList<>(Arrays.asList("стеклянного" , "лоскутного", "ванильного" ,"кузячьего" ,"широкого" ,"прекрасного" ,"кошачьего" ,"волшебного" ,"веселого" ,"лохматого" ,"арбузного" ,"огромного" ,"запойного" ,"бараньего" ,"парадного"));
    private static final ArrayList<String> NAME_4 = new ArrayList<>(Arrays.asList("глаза" ,"плова" ,"Пельша" ,"мира" ,"деда" ,"света" ,"мема" ,"бура" ,"жала" ,"нёба" ,"хлама" ,"шума" ,"неба" ,"фена"));
    private static final int maxSize = NAME_1.size() * NAME_2.size() * NAME_3.size() * NAME_4.size();
    private static final Random rnd = new Random();
    private static final Set<String> names = ConcurrentHashMap.newKeySet();
    
    public static String createUniqueName(){
        checkFull();

        String newName;
        do {
            newName = randomName();
        } while (!addNewName(newName));

        return newName;
    }

    private static String randomName() {
        return NAME_1.get(rnd.nextInt(NAME_1.size())) + " " +
               NAME_2.get(rnd.nextInt(NAME_2.size())) + " " +
               NAME_3.get(rnd.nextInt(NAME_3.size())) + " " +
               NAME_4.get(rnd.nextInt(NAME_4.size()));
    }
    
    private static void checkFull(){
        if (names.size() == maxSize){
            throw new RuntimeException("Пластмассовый мир победил, макет оказался сильней (нехватило имен)...");
        }
    }
    
    private static boolean addNewName(String name){
        return names.add(name);
    }
}

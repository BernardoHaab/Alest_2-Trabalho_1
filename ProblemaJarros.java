import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * Autor: Bernardo Luiz Haab
 * Disciplina: ALEST II
 */

public class ProblemaJarros {
  static Set<String> hist = new HashSet<>();
  static Queue<Integer[]> atuais = new LinkedList<>();

  public static void main(String[] args) {
    try {
      File jarros = new File("jarros.txt");
      Scanner sc = new Scanner(jarros);

      long inicial = new Date().getTime();

      while (sc.hasNextLine()) {
        hist = new HashSet<>();

        Integer[] capTotal = toIntegerArray(sc.nextLine().split(" "));
        Integer[] jIniciais = toIntegerArray(sc.nextLine().split(" "));
        Integer[] jObjetivo = toIntegerArray(sc.nextLine().split(" "));

        Boolean existeSolucao = false;

        System.out.println("Capacidades jarros -> " + Arrays.toString(capTotal));
        System.out.println("Jarro original -> " + Arrays.toString(jIniciais));
        System.out.println("Jarro final -> " + Arrays.toString(jObjetivo));


        if (capTotal[0] <= 40 && capTotal[1] <= 40 && capTotal[2] <= 40) {
          existeSolucao = calculaPassos(capTotal, jIniciais, jObjetivo);
        } else {
          System.out.println("-->Excede capacidade máxima.");
        }


        if (!existeSolucao) {
          System.out.println("***Não existe solução***");
        }
        System.out.println("------");
      }

      long tFinal = new Date().getTime();
      System.out.println("Inicial: " + inicial);
      System.out.println("Final: " + tFinal);
      System.out.println("Intervalo: " + (tFinal - inicial));
    } catch (Exception e) {
      System.out.println("Arquivo não encontrado");
    }

  }

  public static boolean calculaPassos(Integer[] capTotal, Integer[] jIniciais, Integer[] jObjetivo) {
    int passos = 0;
    Queue<Integer[]> testados = new LinkedList<>();
    LinkedList<Integer> listaNumJarros = new LinkedList<>();
    atuais.add(jIniciais);

    do {
      while (atuais.size() > 0) {
        Integer[] jPossivel = atuais.remove();

        if (jPossivel[0] == jObjetivo[0] && jPossivel[1] == jObjetivo[1] && jPossivel[2] == jObjetivo[2]) {
          System.out.println("Passos: " + passos);
          return true;
        }

        testados.add(jPossivel);
      }

      Integer[] jOriginal = testados.remove();
      atuais = geraNovosJarros(jOriginal, capTotal);

      if (listaNumJarros.size() == 0) {
        listaNumJarros.add(atuais.size());
        passos++;
      } else {
        listaNumJarros.set(0, listaNumJarros.get(0) - 1);

        if (listaNumJarros.size() > 1) {
          listaNumJarros.set(1, listaNumJarros.get(1) + atuais.size());
        } else {
          listaNumJarros.add(atuais.size());
          passos++;
        }

        if (listaNumJarros.peek() == 0) {
          listaNumJarros.remove();
        }
      }

    } while (testados.size() > 0 || atuais.size() > 0);

    return false;
  }

  public static Queue<Integer[]> geraNovosJarros(Integer[] jarros, Integer[] capTotal) {
    Queue<Integer[]> novosJarros = new LinkedList<>();

    hist.add(Arrays.toString(jarros));

    for (int posOrigem = 0; posOrigem < 3; posOrigem++) {
      for (int posDest = 0; posDest < 3; posDest++) {
        int capAtual = capTotal[posDest] - jarros[posDest];
        if (posOrigem != posDest && jarros[posOrigem] != 0 && capAtual > 0) {
          Integer[] jNovo = Arrays.copyOf(jarros, jarros.length);

          if (jarros[posOrigem] <= capAtual) {
            jNovo[posDest] += jarros[posOrigem];
            jNovo[posOrigem] = 0;
          } else if (capAtual > 0) {
            jNovo[posDest] += capAtual;
            jNovo[posOrigem] -= capAtual;
          }
          if (hist.add(Arrays.toString(jNovo))) {
            novosJarros.add(jNovo);
          }
        }
      }
    }

    return novosJarros;
  }

  public static Integer[] toIntegerArray(String[] strings) {
    Integer[] integers = new Integer[strings.length];

    for (int i = 0; i < strings.length; i++) {
      integers[i] = Integer.valueOf(strings[i]);
    }

    return integers;
  }

}
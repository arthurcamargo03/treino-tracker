package com.treinotracker.ui;

import com.treinotracker.model.Exercise;
import com.treinotracker.model.WaterLog;
import com.treinotracker.repository.DataStore;
import com.treinotracker.service.WaterService;
import com.treinotracker.service.WeekSummary;
import com.treinotracker.service.WorkoutService;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final WorkoutService workoutService;
    private final WaterService waterService;

    public ConsoleApp() {
        DataStore dataStore = new DataStore("data");
        this.workoutService = new WorkoutService(dataStore);
        this.waterService = new WaterService(dataStore);
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Escolha uma opção: ");
            switch (choice) {
                case 1 -> cadastrarExercicio();
                case 2 -> listarExercicios();
                case 3 -> registrarCarga();
                case 4 -> verProgressao();
                case 5 -> beberAgua();
                case 6 -> verHidratacaoHoje();
                case 7 -> configurarMetas();
                case 8 -> carregarDadosExemplo();
                case 0 -> {
                    running = false;
                    System.out.println("Até mais!");
                }
                default -> System.out.println("Opção inválida, tente novamente.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("=== Treino Tracker ===");
        System.out.println("[1] Cadastrar exercício");
        System.out.println("[2] Listar exercícios");
        System.out.println("[3] Registrar carga da semana");
        System.out.println("[4] Ver progressão de um exercício");
        System.out.println("[5] Beber água");
        System.out.println("[6] Ver hidratação de hoje");
        System.out.println("[7] Configurar meta e tamanho da garrafa");
        System.out.println("[8] Carregar dados de exemplo");
        System.out.println("[0] Sair");
    }

    private void cadastrarExercicio() {
        String name = readLine("Nome do exercício: ");
        String group = readLine("Grupo muscular: ");
        try {
            workoutService.addExercise(name, group);
            System.out.println("Exercício cadastrado com sucesso.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void listarExercicios() {
        List<Exercise> exercises = workoutService.getExercises();
        if (exercises.isEmpty()) {
            System.out.println("Nenhum exercício cadastrado.");
            return;
        }
        for (Exercise exercise : exercises) {
            System.out.println("- " + exercise.getName() + " (" + exercise.getMuscleGroup() + ")");
        }
    }

    private void registrarCarga() {
        String name = readLine("Nome do exercício: ");
        if (!workoutService.exerciseExists(name)) {
            System.out.println("Exercício não encontrado.");
            return;
        }
        int week = readInt("Semana: ");
        double weight = readDouble("Carga (kg): ");
        int reps = readInt("Repetições: ");
        int sets = readInt("Séries: ");
        try {
            workoutService.logSet(name, week, weight, reps, sets);
            System.out.println("Registro salvo.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void verProgressao() {
        String name = readLine("Nome do exercício: ");
        if (!workoutService.exerciseExists(name)) {
            System.out.println("Exercício não encontrado.");
            return;
        }
        List<WeekSummary> progression = workoutService.getProgression(name);
        if (progression.isEmpty()) {
            System.out.println("Nenhum registro para esse exercício.");
            return;
        }

        System.out.printf(Locale.US, "%-7s %-8s %-6s %-7s %-10s %-10s %-5s%n",
                "Semana", "Carga", "Reps", "Séries", "Volume", "1RM est.", "Tend.");
        for (WeekSummary w : progression) {
            System.out.printf(Locale.US, "%-7d %-8.1f %-6d %-7d %-10.1f %-10.1f %-5s%n",
                    w.week, w.weight, w.reps, w.sets, w.volume, w.estimated1RM, trendArrow(w.trendPercent));
        }

        System.out.println();
        System.out.println(veredito(workoutService.isProgressing(name), progression));
    }

    private String trendArrow(Double trendPercent) {
        if (trendPercent == null || trendPercent == 0.0) {
            return "→";
        }
        return trendPercent > 0 ? "↑" : "↓";
    }

    private String veredito(boolean progressing, List<WeekSummary> progression) {
        WeekSummary last = progression.get(progression.size() - 1);
        if (last.trendPercent == null) {
            return "Veredito: ainda não há histórico suficiente para avaliar.";
        }
        if (progressing) {
            return "Veredito: progredindo.";
        }
        if (last.trendPercent < 0) {
            return "Veredito: caiu de rendimento.";
        }
        return "Veredito: estagnado.";
    }

    private void beberAgua() {
        String input = readLine("ml (Enter para garrafa cheia): ");
        if (input.isBlank()) {
            waterService.drinkBottle();
            System.out.println("Garrafa registrada.");
            return;
        }
        try {
            int ml = Integer.parseInt(input.trim());
            waterService.drink(ml);
            System.out.println(ml + "ml registrados.");
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        }
    }

    private void verHidratacaoHoje() {
        WaterLog today = waterService.today();
        double percent = today.percent();
        int blocks = (int) Math.round(Math.min(100, percent) / 5.0);
        blocks = Math.max(0, Math.min(20, blocks));
        String bar = "[" + "#".repeat(blocks) + "-".repeat(20 - blocks) + "]";
        System.out.printf(Locale.US, "%s %.1f%% (%d/%dml)%n", bar, percent, today.getConsumedMl(), today.getGoalMl());
        System.out.println(today.goalReached() ? "Meta atingida!" : "Meta ainda não atingida.");
    }

    private void configurarMetas() {
        int goal = readInt("Nova meta diária (ml): ");
        int bottle = readInt("Novo tamanho da garrafa (ml): ");
        waterService.setDailyGoalMl(goal);
        waterService.setBottleSizeMl(bottle);
        System.out.println("Configurações atualizadas.");
    }

    private void carregarDadosExemplo() {
        try {
            workoutService.seedDemoData();
            System.out.println("Dados de exemplo carregados.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao carregar dados de exemplo: " + e.getMessage());
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, digite um número inteiro.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            try {
                return Double.parseDouble(line.trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido, digite um número.");
            }
        }
    }
}

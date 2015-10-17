// Accepted

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EmailAliases {
    private final Map<String, List<String>> groups = new HashMap<>();

    private String canonic(String email) {
        String[] pair = email.toLowerCase().split("@", 2);
        String domain = pair[1];
        String login = pair[0];
        if ("bmail.com".equals(domain)) {
            login = pair[0].replace(".", "").split("\\+", 2)[0];
        }
        return login + "@" + domain;
    }

    private void put(String email) {
        String canonic = canonic(email);
        groups.computeIfAbsent(canonic, (x) -> new LinkedList<>());
        groups.get(canonic).add(email);
    }

    private void input(InputStream source) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(source));
        int n = Integer.parseInt(reader.readLine());
        for (int i = 0; i < n; i++) {
            String email = reader.readLine();
            put(email);
        }
    }

    private void output() {
        System.out.println(groups.size());
        groups.forEach((x, list) -> {
            System.out.printf("%d ", list.size());
            list.forEach((email) -> {
                System.out.printf("%s ", email);
            });
            System.out.println();
        });
    }

    private void run() {
        try {
            input(System.in);
            output();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EmailAliases().run();
    }
}
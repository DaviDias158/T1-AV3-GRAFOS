// AV3 - TRABALHO 1 - EQUIPE D - GRAFOS - PROFESSOR RICARDO CARRUBI
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.Locale;
import java.util.StringTokenizer;

public class Main {

    static class Edge implements Comparable<Edge> {
        private final int v, w;
        private final double weight;

        public Edge(int v, int w, double weight) {
            this.v = v;
            this.w = w;
            this.weight = weight;
        }

        public int either() {
            return v;
        }

        public int other(int vertex) {
            if (vertex == v) return w;
            else return v;
        }

        @Override
        public int compareTo(Edge that) {
            return Double.compare(this.weight, that.weight);
        }
    }

    static class UF {
        private int[] parent;
        private int[] rank;
        private int count;

        public UF(int N) {
            parent = new int[N];
            rank = new int[N];
            count = N;
            for (int i = 0; i < N; i++) {
                parent[i] = i;
            }
        }

        public int find(int i) {
            if (parent[i] == i) return i;
            return parent[i] = find(parent[i]);
        }

        public boolean connected(int v, int w) {
            return find(v) == find(w);
        }

        public void union(int v, int w) {
            int rootV = find(v);
            int rootW = find(w);

            if (rootV != rootW) {
                if (rank[rootV] < rank[rootW]) {
                    parent[rootV] = rootW;
                } else if (rank[rootV] > rank[rootW]) {
                    parent[rootW] = rootV;
                } else {
                    parent[rootW] = rootV;
                    rank[rootW]++;
                }
                count--;
            }
        }

        public int count() {
            return count;
        }
    }

    public static void main(String[] args) throws IOException {
        // --- BLOCO DE LEITURA HÍBRIDA (ARQUIVO LOCAL VS PLATAFORMA) ---
        BufferedReader br;

        // Testa o caminho se o terminal for aberto na raiz 'T1/'
        File arquivoNaRaiz = new File("dados/entradas_do_problema.txt");
        // Testa o caminho se o terminal for aberto dentro de 'T1/src/'
        File arquivoNoSrc = new File("../dados/entradas_do_problema.txt");

        if (arquivoNaRaiz.exists()) {
            br = new BufferedReader(new FileReader(arquivoNaRaiz));
            System.out.println("[INFO] Lendo dados de: dados/entradas_do_problema.txt");
        } else if (arquivoNoSrc.exists()) {
            br = new BufferedReader(new FileReader(arquivoNoSrc));
            System.out.println("[INFO] Lendo dados de: ../dados/entradas_do_problema.txt");
        } else {
            // Fallback para o Kattis (Entrada padrão do sistema)
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        // --------------------------------------------------------------

        StringTokenizer st;
        String line = br.readLine();
        if (line == null) {
            br.close();
            return;
        }
        int N = Integer.parseInt(line.trim());

        for (int t = 0; t < N; t++) {
            while ((line = br.readLine()) != null && line.trim().isEmpty());
            if (line == null) break;

            st = new StringTokenizer(line);
            int S = Integer.parseInt(st.nextToken());
            int P = Integer.parseInt(st.nextToken());

            int[] x = new int[P];
            int[] y = new int[P];

            for (int i = 0; i < P; i++) {
                st = new StringTokenizer(br.readLine());
                x[i] = Integer.parseInt(st.nextToken());
                y[i] = Integer.parseInt(st.nextToken());
            }

            if (S >= P) {
                System.out.printf(Locale.US, "%.2f\n", 0.00);
                continue;
            }

            PriorityQueue<Edge> pq = new PriorityQueue<Edge>();

            for (int i = 0; i < P; i++) {
                for (int j = i + 1; j < P; j++) {
                    double dist = Math.sqrt(Math.pow(x[i] - x[j], 2) + Math.pow(y[i] - y[j], 2));
                    pq.add(new Edge(i, j, dist));
                }
            }

            UF uf = new UF(P);
            double minD = 0;

            // Kruskal
            while (!pq.isEmpty()) {
                Edge e = pq.poll();
                int v = e.either(), w = e.other(v);

                if (!uf.connected(v, w)) {
                    uf.union(v, w);
                    minD = e.weight;

                    if (uf.count() == S) {
                        break;
                    }
                }
            }

            System.out.printf(Locale.US, "%.2f\n", minD);
        }

        br.close(); // Fecha o leitor de arquivo de forma segura
    }
}
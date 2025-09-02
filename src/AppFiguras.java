import javax.swing.*;
import java.awt.*;

public class AppFiguras extends JFrame {
    private final Lienzo lienzo;
    private final JLabel lblFigura;
    private final JLabel lblArea;
    private final JLabel lblPerimetro;

    private Figura figuraActual = null;

    public AppFiguras() {
        super("Figuras Geométricas — TIA (POO + Herencia)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Figuras");
        JMenuItem miCirculo = new JMenuItem("Círculo");
        JMenuItem miRectangulo = new JMenuItem("Rectángulo");
        JMenuItem miTriangulo = new JMenuItem("Triángulo isósceles");
        JMenuItem miLimpiar = new JMenuItem("Limpiar");
        menu.add(miCirculo);
        menu.add(miRectangulo);
        menu.add(miTriangulo);
        menu.addSeparator();
        menu.add(miLimpiar);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        JPanel info = new JPanel(new GridLayout(3, 1));
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblFigura = new JLabel("Figura: —");
        lblArea = new JLabel("Área: —");
        lblPerimetro = new JLabel("Perímetro: —");
        Font f = new Font("SansSerif", Font.PLAIN, 16);
        lblFigura.setFont(f); lblArea.setFont(f); lblPerimetro.setFont(f);
        info.add(lblFigura); info.add(lblArea); info.add(lblPerimetro);

        lienzo = new Lienzo();
        lienzo.setBackground(Color.WHITE);

        setLayout(new BorderLayout());
        add(info, BorderLayout.WEST);
        add(lienzo, BorderLayout.CENTER);

        miCirculo.addActionListener(e -> crearCirculo());
        miRectangulo.addActionListener(e -> crearRectangulo());
        miTriangulo.addActionListener(e -> crearTrianguloIsosceles());
        miLimpiar.addActionListener(e -> {
            figuraActual = null;
            actualizarEstado(null);
            lienzo.repaint();
        });
    }

    private void crearCirculo() {
        try {
            String sX = JOptionPane.showInputDialog(this, "Centro X (px):", "200");
            String sY = JOptionPane.showInputDialog(this, "Centro Y (px):", "200");
            String sR = JOptionPane.showInputDialog(this, "Radio (px):", "100");
            if (sX == null || sY == null || sR == null) return;
            int x = Integer.parseInt(sX.trim());
            int y = Integer.parseInt(sY.trim());
            double r = Double.parseDouble(sR.trim());
            figuraActual = new Circulo(x, y, r);
            actualizarEstado(figuraActual);
            lienzo.setFigura(figuraActual);
        } catch (NumberFormatException ex) {
            mostrarErrorEntrada();
        }
    }

    private void crearRectangulo() {
        try {
            String sX = JOptionPane.showInputDialog(this, "Posición X (esquina sup izq, px):", "100");
            String sY = JOptionPane.showInputDialog(this, "Posición Y (esquina sup izq, px):", "100");
            String sW = JOptionPane.showInputDialog(this, "Ancho (px):", "200");
            String sH = JOptionPane.showInputDialog(this, "Alto (px):", "120");
            if (sX == null || sY == null || sW == null || sH == null) return;
            int x = Integer.parseInt(sX.trim());
            int y = Integer.parseInt(sY.trim());
            double w = Double.parseDouble(sW.trim());
            double h = Double.parseDouble(sH.trim());
            figuraActual = new Rectangulo(x, y, w, h);
            actualizarEstado(figuraActual);
            lienzo.setFigura(figuraActual);
        } catch (NumberFormatException ex) {
            mostrarErrorEntrada();
        }
    }

    private void crearTrianguloIsosceles() {
        try {
            String sX = JOptionPane.showInputDialog(this, "Centro base X (px):", "300");
            String sY = JOptionPane.showInputDialog(this, "Posición Y (base, px):", "350");
            String sBase = JOptionPane.showInputDialog(this, "Base (px):", "200");
            String sAltura = JOptionPane.showInputDialog(this, "Altura (px):", "150");
            if (sX == null || sY == null || sBase == null || sAltura == null) return;
            int x = Integer.parseInt(sX.trim());
            int y = Integer.parseInt(sY.trim());
            double base = Double.parseDouble(sBase.trim());
            double altura = Double.parseDouble(sAltura.trim());
            figuraActual = new TrianguloIsosceles(x, y, base, altura);
            actualizarEstado(figuraActual);
            lienzo.setFigura(figuraActual);
        } catch (NumberFormatException ex) {
            mostrarErrorEntrada();
        }
    }

    private void mostrarErrorEntrada() {
        JOptionPane.showMessageDialog(this, "Por favor ingrese valores numéricos válidos.",
                "Entrada inválida", JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarEstado(Figura f) {
        if (f == null) {
            lblFigura.setText("Figura: —");
            lblArea.setText("Área: —");
            lblPerimetro.setText("Perímetro: —");
            return;
        }
        lblFigura.setText("Figura: " + f.getNombre());
        lblArea.setText(String.format("Área: %.2f", f.calcularArea()));
        lblPerimetro.setText(String.format("Perímetro: %.2f", f.calcularPerimetro()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppFiguras().setVisible(true));
    }

    public static abstract class Figura {
        protected int posicionX;
        protected int posicionY;

        public Figura(int x, int y) {
            this.posicionX = x;
            this.posicionY = y;
        }

        public abstract double calcularArea();
        public abstract double calcularPerimetro();
        public abstract String getNombre();
        public abstract void dibujar(Graphics2D g2);
    }

    public static class Circulo extends Figura {
        private final double radio;

        public Circulo(int centroX, int centroY, double radio) {
            super(centroX, centroY);
            if (radio <= 0) throw new IllegalArgumentException("El radio debe ser > 0");
            this.radio = radio;
        }

        @Override
        public double calcularArea() {
            return Math.PI * radio * radio;
        }

        @Override
        public double calcularPerimetro() {
            return 2 * Math.PI * radio;
        }

        @Override
        public String getNombre() { return "Círculo"; }

        @Override
        public void dibujar(Graphics2D g2) {
            int diam = (int) Math.round(radio * 2);
            int xSupIzq = (int) Math.round(posicionX - radio);
            int ySupIzq = (int) Math.round(posicionY - radio);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(xSupIzq, ySupIzq, diam, diam);
        }
    }

    public static class Rectangulo extends Figura {
        private final double ancho;
        private final double alto;

        public Rectangulo(int xSupIzq, int ySupIzq, double ancho, double alto) {
            super(xSupIzq, ySupIzq);
            if (ancho <= 0 || alto <= 0) throw new IllegalArgumentException("Dimensiones deben ser > 0");
            this.ancho = ancho; this.alto = alto;
        }

        @Override
        public double calcularArea() { return ancho * alto; }

        @Override
        public double calcularPerimetro() { return 2 * (ancho + alto); }

        @Override
        public String getNombre() { return "Rectángulo"; }

        @Override
        public void dibujar(Graphics2D g2) {
            g2.setStroke(new BasicStroke(2f));
            g2.drawRect(posicionX, posicionY, (int)Math.round(ancho), (int)Math.round(alto));
        }
    }

    public static class TrianguloIsosceles extends Figura {
        private final double base;
        private final double altura;

        public TrianguloIsosceles(int centroBaseX, int baseY, double base, double altura) {
            super(centroBaseX, baseY);
            if (base <= 0 || altura <= 0) throw new IllegalArgumentException("Dimensiones deben ser > 0");
            this.base = base; this.altura = altura;
        }

        @Override
        public double calcularArea() { return (base * altura) / 2.0; }

        @Override
        public double calcularPerimetro() {
            double lado = Math.hypot(base/2.0, altura);
            return base + 2 * lado;
        }

        @Override
        public String getNombre() { return "Triángulo isósceles"; }

        @Override
        public void dibujar(Graphics2D g2) {
            int halfBase = (int) Math.round(base / 2.0);
            int x1 = posicionX - halfBase;
            int y1 = posicionY;
            int x2 = posicionX + halfBase;
            int y2 = posicionY;
            int x3 = posicionX;
            int y3 = (int) Math.round(posicionY - altura);

            Polygon p = new Polygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
            g2.setStroke(new BasicStroke(2f));
            g2.drawPolygon(p);
        }
    }

    public static class Lienzo extends JPanel {
        private Figura figura;

        public void setFigura(Figura f) {
            this.figura = f;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (figura != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                figura.dibujar(g2);
                g2.dispose();
            }
        }
    }
}

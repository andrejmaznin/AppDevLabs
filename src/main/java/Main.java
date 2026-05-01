import adapters.in.gui.MainFrame;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
@ComponentScan(basePackages = {"application", "adapters", "domain"})
@EnableJpaRepositories(basePackages = "adapters.out.persistence")
@EntityScan(basePackages = "adapters.out.persistence")
public class Main {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = context.getBean(MainFrame.class);
            JFrame frame = new JFrame("Управление магическими миссиями");
            frame.setContentPane(mainFrame.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

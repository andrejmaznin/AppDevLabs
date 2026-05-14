import adapters.in.gui.MainFrame;
import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Инициализация приложения...");
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> {
            String value = entry.getValue();
            if (entry.getKey().equals("DB_URL") && value.contains("localhost")) {
                value = value.replace("localhost", "127.0.0.1");
            }
            System.setProperty(entry.getKey(), value);
        });
        logger.debug("Переменные окружения загружены");

        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .headless(false)
                .run(args);

        logger.info("Контекст Spring успешно запущен");

        SwingUtilities.invokeLater(() -> {
            try {
                logger.info("Запуск графического интерфейса...");
                MainFrame mainFrame = context.getBean(MainFrame.class);
                JFrame frame = new JFrame("Управление магическими миссиями");
                frame.setContentPane(mainFrame.getMainPanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(900, 600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                logger.info("Графический интерфейс успешно отображен");
            } catch (Exception e) {
                logger.error("Критическая ошибка при запуске GUI: {}", e.getMessage(), e);
            }
        });
    }
}

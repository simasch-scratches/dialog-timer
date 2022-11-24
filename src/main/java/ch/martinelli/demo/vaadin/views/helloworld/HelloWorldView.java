package ch.martinelli.demo.vaadin.views.helloworld;

import ch.martinelli.demo.vaadin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HelloWorldView extends HorizontalLayout {

    public HelloWorldView(ThreadPoolTaskScheduler scheduler) {
        TextField name = new TextField("Your name");
        Button sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Dialog dialog = new AutoClosableDialog(scheduler, new Span("Hello " + name.getValue()));
            dialog.open();
        });
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
    }

    static class AutoClosableDialog extends Dialog {
        public AutoClosableDialog(ThreadPoolTaskScheduler scheduler, Component... components) {
            super(components);

            scheduler.schedule(() -> getUI().ifPresent(ui -> ui.access(() -> this.close())),
                    LocalDateTime.now().plus(1, ChronoUnit.SECONDS).atZone(ZoneId.systemDefault()).toInstant());
        }
    }

}

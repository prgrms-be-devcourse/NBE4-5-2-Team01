import { EventMountArg, DayCellMountArg } from "@fullcalendar/core";
import { CalendarDate } from "@/types/calendar";
import { createPlusButton } from "@/components/calendar/plusButton";

export function handleEventDidMount(arg: EventMountArg) {
    const albumImage = arg.event.extendedProps.albumImage;

    if (albumImage) {
        arg.el.style.backgroundImage = `url(${albumImage})`;
        arg.el.style.backgroundSize = "cover";
        arg.el.style.backgroundPosition = "center";
        arg.el.style.opacity = "1";
        arg.el.style.pointerEvents = "none";
    }

    const cell = arg.el.closest(".fc-daygrid-day");
    const dateNumber = cell?.querySelector(".fc-daygrid-day-number") as HTMLElement;

    if (dateNumber) {
        dateNumber.style.setProperty("color", "#C8B6FF", "important");
        dateNumber.style.setProperty("font-weight", "700", "important");
        dateNumber.style.setProperty("text-shadow", "0 0 3px rgba(0,0,0,0.5)", "important");
    }
}

export function handleDayCellDidMount(arg: DayCellMountArg, monthly: CalendarDate[]) {
    const cellDate = arg.date.toLocaleDateString("en-CA"); // YYYY-MM-DD
    const currentDate = new Date().toLocaleDateString("en-CA");
    const isAfterToday = cellDate > currentDate;

    if (isAfterToday) return;

    const hasEvent = monthly.some((event: CalendarDate) => event.date === cellDate);
    arg.el.style.position = "relative";

    const button = createPlusButton();
    const cell = arg.el as HTMLElement;

    cell.addEventListener("mouseenter", () => {
        cell.style.backgroundColor = "#D9CFFF";

        if (!hasEvent) {
            cell.appendChild(button);
        }
    });

    cell.addEventListener("mouseleave", () => {
        cell.style.backgroundColor = "";

        if (!hasEvent && button.parentNode) {
            button.parentNode.removeChild(button);
        }
    });
}
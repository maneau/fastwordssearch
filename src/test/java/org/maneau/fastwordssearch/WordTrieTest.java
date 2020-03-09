package org.maneau.fastwordssearch;

import org.junit.Ignore;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class WordTrieTest {
    @Test
    public void addKeywords_add_2_levels() {
        WordTrie dico = new WordTrie();
        dico.addKeyword("manuel vals");
        dico.addKeyword("manuel petit");
        dico.addKeyword("");

        assertNotNull(dico.getNode("manuel"));
        assertEquals(2, dico.getNode("manuel").size());
        assertEquals(2, dico.size());

        dico.addKeyword("manuel petit");
        assertEquals(2, dico.size());
    }

    @Test
    public void search_basic() throws Exception {
        WordTrie dico = WordTrie.builder()
                .addKeyword("manuel vals")
                .addKeyword("manuel petit")
                .addKeyword("jean emmanuel chain")
                .addKeyword("eric petit")
                .build();

        assertNotNull(dico.getNode("manuel"));
        assertEquals(1, dico.parseText("ici eric petit manuel nouveau").size());
        assertEquals(2, dico.parseText("ici eric petit manuel nouveau jean emmanuel chain fin").size());
        assertEquals(2, dico.parseText("ici eric petit manuel nouveau jean emmanuel chain").size());
    }

    @Test
    public void parseText_html() throws Exception {
        String text = "<div id=\"bodyContent\" class=\"mw-body-content\">\n" +
                "\t\t<div id=\"siteSub\" class=\"noprint\">From Wikipedia, the free encyclopedia</div>\n" +
                "\t\t<div id=\"contentSub\"></div>\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\t<div id=\"jump-to-nav\"></div>\n" +
                "\t\t<a class=\"mw-jump-link\" href=\"#mw-head\">Jump to navigation</a>\n" +
                "\t\t<a class=\"mw-jump-link\" href=\"#p-search\">Jump to search</a>\n" +
                "\t\t<div id=\"mw-content-text\" lang=\"en\" dir=\"ltr\" class=\"mw-content-ltr\"><div class=\"mw-parser-output\"><div class=\"shortdescription nomobile noexcerpt noprint searchaux\" style=\"display:none\">Common response to a recurring problem that is usually ineffective or counterproductive</div>\n" +
                "<p>An <b>anti-pattern</b> is a common response to a recurring problem that is usually ineffective and risks being highly counterproductive.<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[1]</a></sup><sup id=\"cite_ref-2\" class=\"reference\"><a href=\"#cite_note-2\">[2]</a></sup> The term, coined in 1995 by <a href=\"/wiki/Andrew_Koenig_(programmer)\" title=\"Andrew Koenig (programmer)\">Andrew Koenig</a>,<sup id=\"cite_ref-3\" class=\"reference\"><a href=\"#cite_note-3\">[3]</a></sup> was inspired by a book, <i><a href=\"/wiki/Design_Patterns_(book)\" class=\"mw-redirect\" title=\"Design Patterns (book)\">Design Patterns</a></i>, which highlights a number of <a href=\"/wiki/Design_pattern\" title=\"Design pattern\">design patterns</a> in <a href=\"/wiki/Software_development\" title=\"Software development\">software development</a> that its authors considered to be highly reliable and effective.\n" +
                "</p><p>The term was popularized three years later by the book <i><a href=\"/wiki/AntiPatterns\" title=\"AntiPatterns\">AntiPatterns</a></i>, which extended its use beyond the field of software design to refer informally to any commonly reinvented but bad solution to a problem. Examples include <a href=\"/wiki/Analysis_paralysis\" title=\"Analysis paralysis\">analysis paralysis</a>, <a href=\"/wiki/Cargo_cult_programming\" title=\"Cargo cult programming\">cargo cult programming</a>, <a href=\"/wiki/Death_march_(software_development)\" class=\"mw-redirect\" title=\"Death march (software development)\">death march</a>, <a href=\"/wiki/Groupthink\" title=\"Groupthink\">groupthink</a> and <a href=\"/wiki/Vendor_lock-in\" title=\"Vendor lock-in\">vendor lock-in</a>.\n" +
                "</p>\n" +
                "<div id=\"toc\" class=\"toc\" role=\"navigation\" aria-labelledby=\"mw-toc-heading\"><input type=\"checkbox\" role=\"button\" id=\"toctogglecheckbox\" class=\"toctogglecheckbox\" style=\"display:none\"><div class=\"toctitle\" lang=\"en\" dir=\"ltr\"><h2 id=\"mw-toc-heading\">Contents</h2><span class=\"toctogglespan\"><label class=\"toctogglelabel\" for=\"toctogglecheckbox\"></label></span></div>\n" +
                "<ul>\n" +
                "<li class=\"toclevel-1 tocsection-1\"><a href=\"#Definition\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">Definition</span></a></li>\n" +
                "<li class=\"toclevel-1 tocsection-2\"><a href=\"#Examples\"><span class=\"tocnumber\">2</span> <span class=\"toctext\">Examples</span></a>\n" +
                "<ul>\n" +
                "<li class=\"toclevel-2 tocsection-3\"><a href=\"#Social_and_business_operations\"><span class=\"tocnumber\">2.1</span> <span class=\"toctext\">Social and business operations</span></a>\n" +
                "<ul>\n" +
                "<li class=\"toclevel-3 tocsection-4\"><a href=\"#Organizational\"><span class=\"tocnumber\">2.1.1</span> <span class=\"toctext\">Organizational</span></a></li>\n" +
                "<li class=\"toclevel-3 tocsection-5\"><a href=\"#Project_management\"><span class=\"tocnumber\">2.1.2</span> <span class=\"toctext\">Project management</span></a></li>\n" +
                "</ul>\n" +
                "</li>\n" +
                "<li class=\"toclevel-2 tocsection-6\"><a href=\"#Software_engineering\"><span class=\"tocnumber\">2.2</span> <span class=\"toctext\">Software engineering</span></a>\n" +
                "<ul>\n" +
                "<li class=\"toclevel-3 tocsection-7\"><a href=\"#Software_design\"><span class=\"tocnumber\">2.2.1</span> <span class=\"toctext\">Software design</span></a></li>\n" +
                "<li class=\"toclevel-3 tocsection-8\"><a href=\"#Object-oriented_programming\"><span class=\"tocnumber\">2.2.2</span> <span class=\"toctext\">Object-oriented programming</span></a></li>\n" +
                "<li class=\"toclevel-3 tocsection-9\"><a href=\"#Programming\"><span class=\"tocnumber\">2.2.3</span> <span class=\"toctext\">Programming</span></a></li>\n" +
                "<li class=\"toclevel-3 tocsection-10\"><a href=\"#Methodological\"><span class=\"tocnumber\">2.2.4</span> <span class=\"toctext\">Methodological</span></a></li>\n" +
                "<li class=\"toclevel-3 tocsection-11\"><a href=\"#Configuration_management\"><span class=\"tocnumber\">2.2.5</span> <span class=\"toctext\">Configuration management</span></a></li>\n" +
                "</ul>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</li>\n" +
                "<li class=\"toclevel-1 tocsection-12\"><a href=\"#See_also\"><span class=\"tocnumber\">3</span> <span class=\"toctext\">See also</span></a></li>\n" +
                "<li class=\"toclevel-1 tocsection-13\"><a href=\"#References\"><span class=\"tocnumber\">4</span> <span class=\"toctext\">References</span></a></li>\n" +
                "<li class=\"toclevel-1 tocsection-14\"><a href=\"#Further_reading\"><span class=\"tocnumber\">5</span> <span class=\"toctext\">Further reading</span></a></li>\n" +
                "<li class=\"toclevel-1 tocsection-15\"><a href=\"#External_links\"><span class=\"tocnumber\">6</span> <span class=\"toctext\">External links</span></a></li>\n" +
                "</ul>\n" +
                "</div>\n" +
                "\n" +
                "<h2><span class=\"mw-headline\" id=\"Definition\">Definition</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=1\" title=\"Edit section: Definition\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h2>\n" +
                "<p>According to the authors of <i>Design Patterns</i>, there must be at least two key elements present to formally distinguish an actual anti-pattern from a simple bad habit, bad practice, or bad idea:\n" +
                "</p>\n" +
                "<ol><li>A commonly used process, structure, or pattern of action that despite initially appearing to be an appropriate and effective response to a problem, has more bad consequences than good ones.</li>\n" +
                "<li>Another solution exists that is documented, repeatable, and proven to be effective.</li></ol>\n" +
                "<h2><span class=\"mw-headline\" id=\"Examples\">Examples</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=2\" title=\"Edit section: Examples\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h2>\n" +
                "<h3><span class=\"mw-headline\" id=\"Social_and_business_operations\">Social and business operations</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=3\" title=\"Edit section: Social and business operations\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h3>\n" +
                "<h4><span class=\"mw-headline\" id=\"Organizational\">Organizational</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=4\" title=\"Edit section: Organizational\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<ul><li><a href=\"/wiki/Analysis_paralysis\" title=\"Analysis paralysis\">Analysis paralysis</a>: A project stalled in the analysis phase, unable to achieve support for any of the potential plans of approach</li>\n" +
                "<li><a href=\"/wiki/Parkinson%27s_law_of_triviality\" class=\"mw-redirect\" title=\"Parkinson's law of triviality\">Bicycle shed</a>: Giving disproportionate weight to trivial issues</li>\n" +
                "<li><a href=\"/wiki/Bleeding_edge\" class=\"mw-redirect\" title=\"Bleeding edge\">Bleeding edge</a>: Operating with cutting-edge technologies that are still untested or unstable leading to cost overruns, under-performance or delayed delivery</li>\n" +
                "<li><a href=\"/wiki/Bystander_apathy\" class=\"mw-redirect\" title=\"Bystander apathy\">Bystander apathy</a>: The phenomenon in which people are less likely to or do not offer help to a person in need when others are present</li>\n" +
                "<li><a href=\"/wiki/Cash_cow\" title=\"Cash cow\">Cash cow</a>: A profitable legacy product that often leads to complacency about new products</li>\n" +
                "<li><a href=\"/wiki/Design_by_committee\" title=\"Design by committee\">Design by committee</a>: The result of having many contributors to a design, but no unifying vision</li>\n" +
                "<li><a href=\"/wiki/Escalation_of_commitment\" title=\"Escalation of commitment\">Escalation of commitment</a>: Failing to revoke a decision when it proves wrong</li>\n" +
                "<li><a href=\"/wiki/Groupthink\" title=\"Groupthink\">Groupthink</a>: A collective state where group members begin to (often unknowingly) think alike and reject differing viewpoints</li>\n" +
                "<li><a href=\"/wiki/Management_by_objectives\" title=\"Management by objectives\">Management by objectives</a>: Management by numbers, focus exclusively on quantitative management criteria, when these are non-essential or cost too much to acquire</li>\n" +
                "<li><a href=\"/wiki/Micromanagement\" title=\"Micromanagement\">Micromanagement</a>: Ineffectiveness from excessive observation, supervision, or other hands-on involvement from management</li>\n" +
                "<li><a href=\"/wiki/Moral_hazard\" title=\"Moral hazard\">Moral hazard</a>: Insulating a decision-maker from the consequences of their decision</li>\n" +
                "<li><a href=\"/wiki/Mushroom_management\" title=\"Mushroom management\">Mushroom management</a>: Keeping employees \"in the dark and fed manure\" (also \"left to stew and finally canned\")</li>\n" +
                "<li><a href=\"/wiki/Peter_principle\" title=\"Peter principle\">Peter principle</a>: Continually promoting otherwise well-performing employees up to their level of incompetence, where they remain indefinitely<sup id=\"cite_ref-4\" class=\"reference\"><a href=\"#cite_note-4\">[4]</a></sup></li>\n" +
                "<li><a href=\"/wiki/Seagull_manager\" class=\"mw-redirect\" title=\"Seagull manager\">Seagull management</a>: Management in which managers only interact with employees when a problem arises, when they \"fly in, make a lot of noise, dump on everyone, do not solve the problem, then fly out\"</li>\n" +
                "<li><a href=\"/wiki/Stovepipe_(organisation)\" title=\"Stovepipe (organisation)\">Stovepipe or Silos</a>: An organizational structure of isolated or semi-isolated teams, in which too many communications take place up and down the hierarchy, rather than directly with other teams across the organization</li>\n" +
                "<li><a href=\"/wiki/Typecasting_(acting)\" class=\"mw-redirect\" title=\"Typecasting (acting)\">Typecasting</a>: Locking successful employees into overly safe, narrowly defined, predictable roles based on their past successes rather than their potential</li>\n" +
                "<li><a href=\"/wiki/Vendor_lock-in\" title=\"Vendor lock-in\">Vendor lock-in</a>: Making a system excessively dependent on an externally supplied component</li></ul>\n" +
                "<h4><span class=\"mw-headline\" id=\"Project_management\">Project management</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=5\" title=\"Edit section: Project management\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<ul><li><a href=\"/wiki/Cart_before_the_horse\" title=\"Cart before the horse\">Cart before the horse</a>: Focusing too many resources on a stage of a project out of its sequence</li>\n" +
                "<li><a href=\"/wiki/Death_march_(software_development)\" class=\"mw-redirect\" title=\"Death march (software development)\">Death march</a>: A project whose staff, while expecting it to fail, are compelled to continue, often with much overwork, by management which is in denial<sup id=\"cite_ref-5\" class=\"reference\"><a href=\"#cite_note-5\">[5]</a></sup></li>\n" +
                "<li><a href=\"/wiki/Ninety-ninety_rule\" title=\"Ninety-ninety rule\">Ninety-ninety rule</a>: Tendency to underestimate the amount of time to complete a project when it is \"nearly done\"</li>\n" +
                "<li><a href=\"/wiki/Overengineering\" title=\"Overengineering\">Overengineering</a>: Spending resources making a project more robust and complex than is needed</li>\n" +
                "<li><a href=\"/wiki/Scope_creep\" title=\"Scope creep\">Scope creep</a>: Uncontrolled changes or continuous growth in a project's scope, or adding new features to the project after the original requirements have been drafted and accepted (also known as requirement creep and <a href=\"/wiki/Feature_creep\" title=\"Feature creep\">feature creep</a>)</li>\n" +
                "<li><a href=\"/wiki/Smoke_and_mirrors\" title=\"Smoke and mirrors\">Smoke and mirrors</a>: Demonstrating unimplemented functions as if they were already implemented</li>\n" +
                "<li><a href=\"/wiki/Brooks%27s_law\" title=\"Brooks's law\">Brooks's law</a>: Adding more resources to a project to increase velocity, when the project is already slowed down by coordination overhead.</li></ul>\n" +
                "<h3><span class=\"mw-headline\" id=\"Software_engineering\">Software engineering</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=6\" title=\"Edit section: Software engineering\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h3>\n" +
                "<h4><span class=\"mw-headline\" id=\"Software_design\">Software design</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=7\" title=\"Edit section: Software design\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<p><span id=\"General_design_anti-patterns\"></span>\n" +
                "</p>\n" +
                "<ul><li><a href=\"/wiki/Abstraction_inversion\" title=\"Abstraction inversion\">Abstraction inversion</a>: Not exposing implemented functionality required by callers of a function/method/constructor, so that the calling code awkwardly re-implements the same functionality in terms of those calls</li>\n" +
                "<li><a href=\"/wiki/Ambiguous_viewpoint\" title=\"Ambiguous viewpoint\">Ambiguous viewpoint</a>: Presenting a model (usually <a href=\"/wiki/Object-oriented_analysis_and_design\" title=\"Object-oriented analysis and design\">Object-oriented analysis and design</a> (OOAD)) without specifying its viewpoint</li>\n" +
                "<li><a href=\"/wiki/Big_ball_of_mud\" title=\"Big ball of mud\">Big ball of mud</a>: A system with no recognizable structure</li>\n" +
                "<li><a href=\"/wiki/Database-as-IPC\" title=\"Database-as-IPC\">Database-as-IPC</a>: Using a database as the message queue for routine <a href=\"/wiki/Inter-process_communication\" title=\"Inter-process communication\">interprocess communication</a> where a much more lightweight mechanism would be suitable</li>\n" +
                "<li><a href=\"/wiki/Gold_plating_(analogy)\" class=\"mw-redirect\" title=\"Gold plating (analogy)\">Gold plating</a>: Continuing to work on a task or project well past the point at which extra effort is not adding value</li>\n" +
                "<li><a href=\"/wiki/Inner-platform_effect\" title=\"Inner-platform effect\">Inner-platform effect</a>: A system so customizable as to become a poor replica of the software development platform</li>\n" +
                "<li><a href=\"/wiki/Input_kludge\" title=\"Input kludge\">Input kludge</a>: Failing to specify and implement the handling of possibly invalid input</li>\n" +
                "<li><a href=\"/wiki/Interface_bloat\" title=\"Interface bloat\">Interface bloat</a>: Making an interface so powerful that it is extremely difficult to implement</li>\n" +
                "<li><a href=\"/wiki/Magic_pushbutton\" title=\"Magic pushbutton\">Magic pushbutton</a>: A form with no dynamic validation or input assistance, such as dropdowns</li>\n" +
                "<li><a href=\"/wiki/Race_hazard\" class=\"mw-redirect\" title=\"Race hazard\">Race hazard</a>: Failing to see the consequences of events that can sometimes interfere with each other</li>\n" +
                "<li><a href=\"/wiki/Stovepipe_system\" title=\"Stovepipe system\">Stovepipe system</a>: A barely maintainable assemblage of ill-related components</li></ul>\n" +
                "<h4><span class=\"mw-headline\" id=\"Object-oriented_programming\">Object-oriented programming</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=8\" title=\"Edit section: Object-oriented programming\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<ul><li><a href=\"/wiki/Anemic_domain_model\" title=\"Anemic domain model\">Anemic domain model</a>: The use of the <a href=\"/wiki/Domain_model\" title=\"Domain model\">domain model</a> without any <a href=\"/wiki/Business_logic\" title=\"Business logic\">business logic</a>.  The domain model's objects cannot guarantee their correctness at any moment, because their validation and mutation logic is placed somewhere outside (most likely in multiple places). Martin Fowler considers this to be an anti-pattern, but some disagree that it is always an anti-pattern.<sup id=\"cite_ref-6\" class=\"reference\"><a href=\"#cite_note-6\">[6]</a></sup></li>\n" +
                "<li><a href=\"/wiki/Call_super\" title=\"Call super\">Call super</a>: Requiring subclasses to call a superclass's overridden method</li>\n" +
                "<li><a href=\"/wiki/Circle%E2%80%93ellipse_problem\" title=\"Circle–ellipse problem\">Circle–ellipse problem</a>: <a href=\"/wiki/Subtype\" class=\"mw-redirect\" title=\"Subtype\">Subtyping</a> variable-types on the basis of value-subtypes</li>\n" +
                "<li><a href=\"/wiki/Circular_dependency\" title=\"Circular dependency\">Circular dependency</a>: Introducing unnecessary direct or indirect mutual dependencies between objects or software modules</li>\n" +
                "<li><a href=\"/wiki/Constant_interface\" title=\"Constant interface\">Constant interface</a>: Using interfaces to define constants</li>\n" +
                "<li><a href=\"/wiki/God_object\" title=\"God object\">God object</a>: Concentrating too many functions in a single part of the design (class)</li>\n" +
                "<li><a href=\"/wiki/Object_cesspool\" class=\"mw-redirect\" title=\"Object cesspool\">Object cesspool</a>: Reusing objects whose state does not conform to the (possibly implicit) contract for re-use</li>\n" +
                "<li><a href=\"/wiki/Object_orgy\" title=\"Object orgy\">Object orgy</a>: Failing to properly encapsulate objects permitting unrestricted access to their internals</li>\n" +
                "<li><a href=\"/wiki/Poltergeist_(computer_science)\" class=\"mw-redirect\" title=\"Poltergeist (computer science)\">Poltergeists</a>: Objects whose sole purpose is to pass information to another object</li>\n" +
                "<li><a href=\"/wiki/Sequential_coupling\" title=\"Sequential coupling\">Sequential coupling</a>: A class that requires its methods to be called in a particular order</li>\n" +
                "<li><a href=\"/wiki/Yo-yo_problem\" title=\"Yo-yo problem\">Yo-yo problem</a>: A structure (e.g., of inheritance) that is hard to understand due to excessive fragmentation</li></ul>\n" +
                "<h4><span class=\"mw-headline\" id=\"Programming\">Programming</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=9\" title=\"Edit section: Programming\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<ul><li><a href=\"/wiki/Accidental_complexity\" class=\"mw-redirect\" title=\"Accidental complexity\">Accidental complexity</a>: Programming tasks which could be eliminated with better tools (as opposed to essential complexity inherent in the problem being solved)</li>\n" +
                "<li><a href=\"/wiki/Action_at_a_distance_(computer_science)\" class=\"mw-redirect\" title=\"Action at a distance (computer science)\">Action at a distance</a>: Unexpected interaction between widely separated parts of a system</li>\n" +
                "<li><a href=\"/wiki/Boat_anchor_(computer_science)\" class=\"mw-redirect\" title=\"Boat anchor (computer science)\">Boat anchor</a>: Retaining a part of a system that no longer has any use</li>\n" +
                "<li><a href=\"/wiki/Busy_waiting\" title=\"Busy waiting\">Busy waiting</a>: Consuming <a href=\"/wiki/CPU\" class=\"mw-redirect\" title=\"CPU\">CPU</a> while waiting for something to happen, usually by repeated checking instead of messaging</li>\n" +
                "<li><a href=\"/wiki/Caching_failure\" class=\"mw-redirect\" title=\"Caching failure\">Caching failure</a>: Forgetting to clear a cache that holds a negative result (error) after the error condition has been corrected</li>\n" +
                "<li><a href=\"/wiki/Cargo_cult_programming\" title=\"Cargo cult programming\">Cargo cult programming</a>: Using patterns and methods without understanding why</li>\n" +
                "<li><a href=\"/wiki/Coding_by_exception\" title=\"Coding by exception\">Coding by exception</a>: Adding new code to handle each special case as it is recognized</li>\n" +
                "<li><a href=\"/wiki/Design_pattern\" title=\"Design pattern\">Design pattern</a>: The use of patterns has itself been called an anti-pattern, a sign that a system is not employing enough <a href=\"/wiki/Abstraction_principle_(computer_programming)\" title=\"Abstraction principle (computer programming)\">abstraction</a><sup id=\"cite_ref-7\" class=\"reference\"><a href=\"#cite_note-7\">[7]</a></sup></li>\n" +
                "<li><a href=\"/wiki/Error_hiding\" title=\"Error hiding\">Error hiding</a>: Catching an error message before it can be shown to the user and either showing nothing or showing a meaningless message. This anti-pattern is also named <i>Diaper Pattern</i>. Also can refer to erasing the <a href=\"/wiki/Stack_trace\" title=\"Stack trace\">Stack trace</a> during exception handling, which can hamper debugging.</li>\n" +
                "<li><a href=\"/wiki/Hard_code\" class=\"mw-redirect\" title=\"Hard code\">Hard code</a>: Embedding assumptions about the environment of a system in its implementation</li>\n" +
                "<li><a href=\"/wiki/Spaghetti_code#Lasagna_code\" title=\"Spaghetti code\">Lasagna code</a>: Programs whose structure consists of too many layers of inheritance</li>\n" +
                "<li><a href=\"/wiki/Lava_flow_(programming)\" title=\"Lava flow (programming)\">Lava flow</a>: Retaining undesirable (redundant or low-quality) code because removing it is too expensive or has unpredictable consequences<sup id=\"cite_ref-8\" class=\"reference\"><a href=\"#cite_note-8\">[8]</a></sup><sup id=\"cite_ref-9\" class=\"reference\"><a href=\"#cite_note-9\">[9]</a></sup></li>\n" +
                "<li><a href=\"/wiki/Loop-switch_sequence\" title=\"Loop-switch sequence\">Loop-switch sequence</a>: Encoding a set of sequential steps using a switch within a loop statement</li>\n" +
                "<li><a href=\"/wiki/Magic_number_(programming)#Unnamed_numerical_constants\" title=\"Magic number (programming)\">Magic numbers</a>: Including unexplained numbers in algorithms</li>\n" +
                "<li><a href=\"/wiki/Magic_string_(programming)#Magic_strings_in_code\" class=\"mw-redirect\" title=\"Magic string (programming)\">Magic strings</a>: Implementing presumably unlikely input scenarios, such as comparisons with very specific strings, to mask functionality.</li>\n" +
                "<li><a href=\"/wiki/Don%27t_Repeat_Yourself\" class=\"mw-redirect\" title=\"Don't Repeat Yourself\">Repeating yourself</a>: Writing code which contains repetitive patterns and substrings over again; avoid with <a href=\"/wiki/Once_and_only_once\" class=\"mw-redirect\" title=\"Once and only once\">once and only once</a> (abstraction principle)</li>\n" +
                "<li><a href=\"/wiki/Shooting_the_messenger\" title=\"Shooting the messenger\">Shooting the messenger</a>: Throwing exceptions from the scope of a plugin or subscriber in response to legitimate input, especially when this causes the outer scope to fail.</li>\n" +
                "<li><a href=\"/wiki/Shotgun_surgery\" title=\"Shotgun surgery\">Shotgun surgery</a>: Developer adds features to an application codebase which span a multiplicity of implementors or implementations in a single change</li>\n" +
                "<li><a href=\"/wiki/Softcoding\" title=\"Softcoding\">Soft code</a>: Storing business logic in configuration files rather than source code<sup id=\"cite_ref-10\" class=\"reference\"><a href=\"#cite_note-10\">[10]</a></sup></li>\n" +
                "<li><a href=\"/wiki/Spaghetti_code\" title=\"Spaghetti code\">Spaghetti code</a>: Programs whose structure is barely comprehensible, especially because of misuse of code structures</li></ul>\n" +
                "<h4><span class=\"mw-headline\" id=\"Methodological\">Methodological</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=10\" title=\"Edit section: Methodological\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<ul><li><a href=\"/wiki/Copy_and_paste_programming\" class=\"mw-redirect\" title=\"Copy and paste programming\">Copy and paste programming</a>: Copying (and modifying) existing code rather than creating generic solutions</li>\n" +
                "<li><a href=\"/wiki/Every_Fool_His_Own_Tool\" title=\"Every Fool His Own Tool\">Every Fool Their Own Tool</a>: Failing to use proper software development principles when creating tools to facilitate the software development process itself.<sup id=\"cite_ref-11\" class=\"reference\"><a href=\"#cite_note-11\">[11]</a></sup><sup class=\"noprint Inline-Template\" style=\"white-space:nowrap;\">[<i><a href=\"/wiki/Wikipedia:No_original_research\" title=\"Wikipedia:No original research\"><span title=\"The material near this tag possibly contains original research. (March 2018)\">original research?</span></a></i>]</sup></li>\n" +
                "<li><a href=\"/wiki/Golden_hammer\" class=\"mw-redirect\" title=\"Golden hammer\">Golden hammer</a>: Assuming that a favorite solution is universally applicable (See: <a href=\"/wiki/Silver_bullet\" title=\"Silver bullet\">Silver bullet</a>)</li>\n" +
                "<li><a href=\"/wiki/Improbability_factor\" title=\"Improbability factor\">Improbability factor</a>: Assuming that it is improbable that a known error will occur</li>\n" +
                "<li><a href=\"/wiki/Invented_here\" title=\"Invented here\">Invented here</a>: The tendency towards dismissing any innovation or less than trivial solution originating from inside the organization, usually because of lack of confidence in the staff</li>\n" +
                "<li><a href=\"/wiki/Not_Invented_Here#In_computing\" class=\"mw-redirect\" title=\"Not Invented Here\">Not Invented Here</a> (NIH) syndrome: The tendency towards <i><a href=\"/wiki/Reinventing_the_wheel\" title=\"Reinventing the wheel\">reinventing the wheel</a></i> (failing to adopt an existing, adequate solution)</li>\n" +
                "<li><a href=\"/wiki/Optimization_(computer_science)#When_to_optimize\" class=\"mw-redirect\" title=\"Optimization (computer science)\">Premature optimization</a>: Coding early-on for perceived efficiency, sacrificing good design, maintainability, and sometimes even real-world efficiency</li>\n" +
                "<li><a href=\"/wiki/Programming_by_permutation\" title=\"Programming by permutation\">Programming by permutation</a> (or \"programming by accident\", or \"programming by coincidence\"): Trying to approach a solution by successively modifying the code to see if it works</li>\n" +
                "<li><a href=\"/wiki/Reinventing_the_square_wheel\" class=\"mw-redirect\" title=\"Reinventing the square wheel\">Reinventing the square wheel</a>: Failing to adopt an existing solution and instead adopting a custom solution which performs much worse than the existing one</li>\n" +
                "<li><a href=\"/wiki/No_Silver_Bullet\" title=\"No Silver Bullet\">Silver bullet</a>: Assuming that a favorite technical solution can solve a larger process or problem</li>\n" +
                "<li><a href=\"/wiki/Tester_Driven_Development\" title=\"Tester Driven Development\">Tester Driven Development</a>: Software projects in which new requirements are specified in bug reports</li></ul>\n" +
                "<h4><span class=\"mw-headline\" id=\"Configuration_management\">Configuration management</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=11\" title=\"Edit section: Configuration management\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h4>\n" +
                "<ul><li><a href=\"/wiki/Dependency_hell\" title=\"Dependency hell\">Dependency hell</a>: Problems with versions of required products</li>\n" +
                "<li><a href=\"/wiki/DLL_hell\" class=\"mw-redirect\" title=\"DLL hell\">DLL hell</a>: Inadequate management of <a href=\"/wiki/Dynamic-link_libraries\" class=\"mw-redirect\" title=\"Dynamic-link libraries\">dynamic-link libraries</a> (DLLs), specifically on <a href=\"/wiki/Microsoft_Windows\" title=\"Microsoft Windows\">Microsoft Windows</a></li>\n" +
                "<li><a href=\"/wiki/Extension_conflict\" title=\"Extension conflict\">Extension conflict</a>: Problems with different extensions to <a href=\"/wiki/Classic_Mac_OS\" title=\"Classic Mac OS\">classic Mac OS</a> attempting to patch the same parts of the operating system</li>\n" +
                "<li><a href=\"/wiki/JAR_hell\" class=\"mw-redirect\" title=\"JAR hell\">JAR hell</a>: Overutilization of multiple <a href=\"/wiki/JAR_(file_format)\" title=\"JAR (file format)\">JAR</a> files, usually causing versioning and location problems because of misunderstanding of the <a href=\"/wiki/Java_Classloader\" title=\"Java Classloader\">Java class loading</a> model</li></ul>\n" +
                "<h2><span class=\"mw-headline\" id=\"See_also\">See also</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=12\" title=\"Edit section: See also\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h2>\n" +
                "<ul><li><a href=\"/wiki/Code_smell\" title=\"Code smell\">Code smell</a> – symptom of unsound programming</li>\n" +
                "<li><a href=\"/wiki/Design_smell\" title=\"Design smell\">Design smell</a></li>\n" +
                "<li><a href=\"/wiki/List_of_software_development_philosophies\" title=\"List of software development philosophies\">List of software development philosophies</a> – approaches, styles, maxims and philosophies for software development</li>\n" +
                "<li><a href=\"/wiki/List_of_tools_for_static_code_analysis\" title=\"List of tools for static code analysis\">List of tools for static code analysis</a></li>\n" +
                "<li><a href=\"/wiki/Software_rot\" title=\"Software rot\">Software rot</a></li>\n" +
                "<li><a href=\"/wiki/Software_Peter_principle\" title=\"Software Peter principle\">Software Peter principle</a></li>\n" +
                "<li><a href=\"/wiki/Capability_Immaturity_Model\" title=\"Capability Immaturity Model\">Capability Immaturity Model</a></li>\n" +
                "<li><a href=\"/wiki/ISO_29110\" title=\"ISO 29110\">ISO 29110</a>: Software Life Cycle Profiles and Guidelines for Very Small Entities (VSEs)</li>\n" +
                "<li><i><a href=\"/wiki/The_Innovator%27s_Dilemma\" title=\"The Innovator's Dilemma\">The Innovator's Dilemma</a></i></li></ul>\n" +
                "<h2><span class=\"mw-headline\" id=\"References\">References</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=13\" title=\"Edit section: References\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h2>\n" +
                "<div class=\"reflist columns references-column-width\" style=\"-moz-column-width: 30em; -webkit-column-width: 30em; column-width: 30em; list-style-type: decimal;\">\n" +
                "<ol class=\"references\">\n" +
                "<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-1\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\">\n" +
                "<cite class=\"citation book\">Budgen, D. (2003). <a rel=\"nofollow\" class=\"external text\" href=\"https://books.google.com/books?id=bnY3vb606bAC&amp;pg=PA225&amp;dq=%22anti-pattern%22+date:1990-2003\"><i>Software design</i></a>. Harlow, Eng.: Addison-Wesley. p.&nbsp;225. <a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/0-201-72219-4\" title=\"Special:BookSources/0-201-72219-4\"><bdi>0-201-72219-4</bdi></a>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=book&amp;rft.btitle=Software+design&amp;rft.place=Harlow%2C+Eng.&amp;rft.pages=225&amp;rft.pub=Addison-Wesley&amp;rft.date=2003&amp;rft.isbn=0-201-72219-4&amp;rft.au=Budgen%2C+D.&amp;rft_id=https%3A%2F%2Fbooks.google.com%2Fbooks%3Fid%3DbnY3vb606bAC%26pg%3DPA225%26dq%3D%2522anti-pattern%2522%2Bdate%3A1990-2003&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><style data-mw-deduplicate=\"TemplateStyles:r935243608\">.mw-parser-output cite.citation{font-style:inherit}.mw-parser-output .citation q{quotes:\"\\\"\"\"\\\"\"\"'\"\"'\"}.mw-parser-output .id-lock-free a,.mw-parser-output .citation .cs1-lock-free a{background:url(\"//upload.wikimedia.org/wikipedia/commons/thumb/6/65/Lock-green.svg/9px-Lock-green.svg.png\")no-repeat;background-position:right .1em center}.mw-parser-output .id-lock-limited a,.mw-parser-output .id-lock-registration a,.mw-parser-output .citation .cs1-lock-limited a,.mw-parser-output .citation .cs1-lock-registration a{background:url(\"//upload.wikimedia.org/wikipedia/commons/thumb/d/d6/Lock-gray-alt-2.svg/9px-Lock-gray-alt-2.svg.png\")no-repeat;background-position:right .1em center}.mw-parser-output .id-lock-subscription a,.mw-parser-output .citation .cs1-lock-subscription a{background:url(\"//upload.wikimedia.org/wikipedia/commons/thumb/a/aa/Lock-red-alt-2.svg/9px-Lock-red-alt-2.svg.png\")no-repeat;background-position:right .1em center}.mw-parser-output .cs1-subscription,.mw-parser-output .cs1-registration{color:#555}.mw-parser-output .cs1-subscription span,.mw-parser-output .cs1-registration span{border-bottom:1px dotted;cursor:help}.mw-parser-output .cs1-ws-icon a{background:url(\"//upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Wikisource-logo.svg/12px-Wikisource-logo.svg.png\")no-repeat;background-position:right .1em center}.mw-parser-output code.cs1-code{color:inherit;background:inherit;border:inherit;padding:inherit}.mw-parser-output .cs1-hidden-error{display:none;font-size:100%}.mw-parser-output .cs1-visible-error{font-size:100%}.mw-parser-output .cs1-maint{display:none;color:#33aa33;margin-left:0.3em}.mw-parser-output .cs1-subscription,.mw-parser-output .cs1-registration,.mw-parser-output .cs1-format{font-size:95%}.mw-parser-output .cs1-kern-left,.mw-parser-output .cs1-kern-wl-left{padding-left:0.2em}.mw-parser-output .cs1-kern-right,.mw-parser-output .cs1-kern-wl-right{padding-right:0.2em}</style> \"As described in Long (2001), design anti-patterns are 'obvious, but wrong, solutions to recurring problems'.\"</span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-2\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-2\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\">\n" +
                "<cite class=\"citation book\"><a href=\"/wiki/Scott_W._Ambler\" class=\"mw-redirect\" title=\"Scott W. Ambler\">Scott W. Ambler</a> (1998). <a rel=\"nofollow\" class=\"external text\" href=\"https://books.google.com/books?id=qJJk2yEeoZoC&amp;pg=PA4&amp;dq=%22anti-pattern%22+date:1990-2001\"><i>Process patterns: building large-scale systems using object technology</i></a>. Cambridge, UK: Cambridge University Press. p.&nbsp;4. <a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/0-521-64568-9\" title=\"Special:BookSources/0-521-64568-9\"><bdi>0-521-64568-9</bdi></a>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=book&amp;rft.btitle=Process+patterns%3A+building+large-scale+systems+using+object+technology&amp;rft.place=Cambridge%2C+UK&amp;rft.pages=4&amp;rft.pub=Cambridge+University+Press&amp;rft.date=1998&amp;rft.isbn=0-521-64568-9&amp;rft.au=Scott+W.+Ambler&amp;rft_id=https%3A%2F%2Fbooks.google.com%2Fbooks%3Fid%3DqJJk2yEeoZoC%26pg%3DPA4%26dq%3D%2522anti-pattern%2522%2Bdate%3A1990-2001&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"> \"...common approaches to solving recurring problems that prove to be ineffective. These approaches are called antipatterns.\"</span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-3\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-3\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\">\n" +
                "<cite class=\"citation journal\">Koenig, Andrew (March–April 1995). \"Patterns and Antipatterns\". <i>Journal of Object-Oriented Programming</i>. <b>8</b> (1): 46–48.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Ajournal&amp;rft.genre=article&amp;rft.jtitle=Journal+of+Object-Oriented+Programming&amp;rft.atitle=Patterns+and+Antipatterns&amp;rft.volume=8&amp;rft.issue=1&amp;rft.pages=46-48&amp;rft.date=1995-03%2F1995-04&amp;rft.aulast=Koenig&amp;rft.aufirst=Andrew&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\">; was later re-printed in the: <cite class=\"citation book\">Rising, Linda (1998). <a rel=\"nofollow\" class=\"external text\" href=\"https://books.google.com/books?id=HBAuixGMYWEC&amp;pg=PT1&amp;dq=0-521-64818-1\"><i>The patterns handbook: techniques, strategies, and applications</i></a>. Cambridge, U.K.: Cambridge University Press. p.&nbsp;387. <a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/0-521-64818-1\" title=\"Special:BookSources/0-521-64818-1\"><bdi>0-521-64818-1</bdi></a>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=book&amp;rft.btitle=The+patterns+handbook%3A+techniques%2C+strategies%2C+and+applications&amp;rft.place=Cambridge%2C+U.K.&amp;rft.pages=387&amp;rft.pub=Cambridge+University+Press&amp;rft.date=1998&amp;rft.isbn=0-521-64818-1&amp;rft.au=Rising%2C+Linda&amp;rft_id=https%3A%2F%2Fbooks.google.com%2Fbooks%3Fid%3DHBAuixGMYWEC%26pg%3DPT1%26dq%3D0-521-64818-1&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\">  \"An antipattern is just like a pattern, except that instead of a solution it gives something that looks superficially like a solution, but isn't one.\"</span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-4\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-4\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\">Peter, Lawrence J. (1969), <i>The Peter Principle: Why Things Always Go Wrong</i>; 1969 Buccaneer Books, <link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"><a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/9781568491615\" title=\"Special:BookSources/9781568491615\">9781568491615</a></span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-5\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-5\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\">Yourdon, Edward (1997), <i>Death March</i>;  <link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"><a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/978-0137483105\" title=\"Special:BookSources/978-0137483105\">978-0137483105</a></span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-6\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-6\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\"><cite class=\"citation web\"><a rel=\"nofollow\" class=\"external text\" href=\"https://blog.inf.ed.ac.uk/sapm/2014/02/04/the-anaemic-domain-model-is-no-anti-pattern-its-a-solid-design/\">\"The Anaemic Domain Model is no Anti-Pattern, it's a SOLID design\"</a>. <i>SAPM: Course blog</i>. 4 February 2014<span class=\"reference-accessdate\">. Retrieved <span class=\"nowrap\">3 January</span> 2015</span>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Ajournal&amp;rft.genre=unknown&amp;rft.jtitle=SAPM%3A+Course+blog&amp;rft.atitle=The+Anaemic+Domain+Model+is+no+Anti-Pattern%2C+it%E2%80%99s+a+SOLID+design&amp;rft.date=2014-02-04&amp;rft_id=https%3A%2F%2Fblog.inf.ed.ac.uk%2Fsapm%2F2014%2F02%2F04%2Fthe-anaemic-domain-model-is-no-anti-pattern-its-a-solid-design%2F&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-7\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-7\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\"><cite class=\"citation web\"><a rel=\"nofollow\" class=\"external text\" href=\"http://www.paulgraham.com/icad.html\">\"Revenge of the Nerds\"</a>. <q>In the OO world you hear a good deal about \"patterns\". I wonder if these patterns are not sometimes evidence of case (c), the human compiler, at work. When I see patterns in my programs, I consider it a sign of trouble. The shape of a program should reflect only the problem it needs to solve. Any other regularity in the code is a sign, to me at least, that I'm using abstractions that aren't powerful enough— often that I'm generating by hand the expansions of some macro that I need to write.</q></cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=unknown&amp;rft.btitle=Revenge+of+the+Nerds&amp;rft_id=http%3A%2F%2Fwww.paulgraham.com%2Ficad.html&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-8\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-8\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\"><a rel=\"nofollow\" class=\"external text\" href=\"http://www.antipatterns.com/lavaflow.htm\">Lava Flow</a> at antipatterns.com</span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-9\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-9\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\"><cite class=\"citation web\"><a rel=\"nofollow\" class=\"external text\" href=\"https://web.archive.org/web/20110311103207/http://www.icmgworld.com/corp/news/Articles/RS/jan_0202.asp\">\"Undocumented 'lava flow' antipatterns complicate process\"</a>. Icmgworld.com. 14 January 2002. Archived from <a rel=\"nofollow\" class=\"external text\" href=\"http://www.icmgworld.com/corp/news/Articles/RS/jan_0202.asp\">the original</a> on 11 March 2011<span class=\"reference-accessdate\">. Retrieved <span class=\"nowrap\">3 May</span> 2010</span>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=unknown&amp;rft.btitle=Undocumented+%27lava+flow%27+antipatterns+complicate+process&amp;rft.pub=Icmgworld.com&amp;rft.date=2002-01-14&amp;rft_id=http%3A%2F%2Fwww.icmgworld.com%2Fcorp%2Fnews%2FArticles%2FRS%2Fjan_0202.asp&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-10\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-10\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\"><cite class=\"citation web\">Papadimoulis, Alex (10 April 2007). <a rel=\"nofollow\" class=\"external text\" href=\"http://thedailywtf.com/Articles/Soft_Coding.aspx\">\"Soft Coding\"</a>. thedailywtf.com<span class=\"reference-accessdate\">. Retrieved <span class=\"nowrap\">27 June</span> 2011</span>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=unknown&amp;rft.btitle=Soft+Coding&amp;rft.pub=thedailywtf.com&amp;rft.date=2007-04-10&amp;rft.aulast=Papadimoulis&amp;rft.aufirst=Alex&amp;rft_id=http%3A%2F%2Fthedailywtf.com%2FArticles%2FSoft_Coding.aspx&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></span>\n" +
                "</li>\n" +
                "<li id=\"cite_note-11\"><span class=\"mw-cite-backlink\"><b><a href=\"#cite_ref-11\" aria-label=\"Jump up\" title=\"Jump up\">^</a></b></span> <span class=\"reference-text\"><cite class=\"citation web\"><a rel=\"nofollow\" class=\"external text\" href=\"https://www.linkedin.com/pulse/every-fool-his-own-tool-marcel-heemskerk-msc-scea\">\"Every Fool His Own Tool\"</a>. <i>linkedin.com</i>. 23 January 2017.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Ajournal&amp;rft.genre=unknown&amp;rft.jtitle=linkedin.com&amp;rft.atitle=Every+Fool+His+Own+Tool&amp;rft.date=2017-01-23&amp;rft_id=https%3A%2F%2Fwww.linkedin.com%2Fpulse%2Fevery-fool-his-own-tool-marcel-heemskerk-msc-scea&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></span>\n" +
                "</li>\n" +
                "</ol></div>\n" +
                "<h2><span class=\"mw-headline\" id=\"Further_reading\">Further reading</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=14\" title=\"Edit section: Further reading\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h2>\n" +
                "<ol><li><cite class=\"citation book\">Laplante, Phillip A.; Neill, Colin J. (2005). <i>Antipatterns: Identification, Refactoring and Management</i>. Auerbach Publications. <a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/0-8493-2994-9\" title=\"Special:BookSources/0-8493-2994-9\"><bdi>0-8493-2994-9</bdi></a>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=book&amp;rft.btitle=Antipatterns%3A+Identification%2C+Refactoring+and+Management&amp;rft.pub=Auerbach+Publications&amp;rft.date=2005&amp;rft.isbn=0-8493-2994-9&amp;rft.aulast=Laplante&amp;rft.aufirst=Phillip+A.&amp;rft.au=Neill%2C+Colin+J.&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></li>\n" +
                "<li><cite class=\"citation book\">Brown, William J.; Malveau, Raphael C.; McCormick, Hays W.; Thomas, Scott W. (2000).  Hudson, Theresa Hudson (ed.). <i>Anti-Patterns in Project Management</i>. <a href=\"/wiki/John_Wiley_%26_Sons\" class=\"mw-redirect\" title=\"John Wiley &amp; Sons\">John Wiley &amp; Sons</a>. <a href=\"/wiki/International_Standard_Book_Number\" title=\"International Standard Book Number\">ISBN</a>&nbsp;<a href=\"/wiki/Special:BookSources/0-471-36366-9\" title=\"Special:BookSources/0-471-36366-9\"><bdi>0-471-36366-9</bdi></a>.</cite><span title=\"ctx_ver=Z39.88-2004&amp;rft_val_fmt=info%3Aofi%2Ffmt%3Akev%3Amtx%3Abook&amp;rft.genre=book&amp;rft.btitle=Anti-Patterns+in+Project+Management&amp;rft.pub=John+Wiley+%26+Sons&amp;rft.date=2000&amp;rft.isbn=0-471-36366-9&amp;rft.aulast=Brown&amp;rft.aufirst=William+J.&amp;rft.au=Malveau%2C+Raphael+C.&amp;rft.au=McCormick%2C+Hays+W.&amp;rft.au=Thomas%2C+Scott+W.&amp;rfr_id=info%3Asid%2Fen.wikipedia.org%3AAnti-pattern\" class=\"Z3988\"></span><link rel=\"mw-deduplicated-inline-style\" href=\"mw-data:TemplateStyles:r935243608\"></li></ol>\n" +
                "<h2><span class=\"mw-headline\" id=\"External_links\">External links</span><span class=\"mw-editsection\"><span class=\"mw-editsection-bracket\">[</span><a href=\"/w/index.php?title=Anti-pattern&amp;action=edit&amp;section=15\" title=\"Edit section: External links\">edit</a><span class=\"mw-editsection-bracket\">]</span></span></h2>\n" +
                "<table role=\"presentation\" class=\"mbox-small plainlinks sistersitebox\" style=\"background-color:#f9f9f9;border:1px solid #aaa;color:#000\">\n" +
                "<tbody><tr>\n" +
                "<td class=\"mbox-image\"><img alt=\"\" src=\"//upload.wikimedia.org/wikipedia/en/thumb/4/4a/Commons-logo.svg/30px-Commons-logo.svg.png\" decoding=\"async\" width=\"30\" height=\"40\" class=\"noviewer\" srcset=\"//upload.wikimedia.org/wikipedia/en/thumb/4/4a/Commons-logo.svg/45px-Commons-logo.svg.png 1.5x, //upload.wikimedia.org/wikipedia/en/thumb/4/4a/Commons-logo.svg/59px-Commons-logo.svg.png 2x\" data-file-width=\"1024\" data-file-height=\"1376\"></td>\n" +
                "<td class=\"mbox-text plainlist\">Wikimedia Commons has media related to <i><b><a href=\"https://commons.wikimedia.org/wiki/Category:Anti-patterns\" class=\"extiw\" title=\"commons:Category:Anti-patterns\"><span style=\"\">Anti-patterns</span></a></b></i>.</td></tr>\n" +
                "</tbody></table>\n" +
                "<ul><li><a rel=\"nofollow\" class=\"external text\" href=\"http://c2.com/cgi/wiki?AntiPattern\">Anti-pattern</a> at <a href=\"/wiki/WikiWikiWeb\" title=\"WikiWikiWeb\">WikiWikiWeb</a></li>\n" +
                "<li><a rel=\"nofollow\" class=\"external text\" href=\"http://c2.com/cgi/wiki?AntiPatternsCatalog\">Anti-patterns catalog</a></li>\n" +
                "<li><a rel=\"nofollow\" class=\"external text\" href=\"http://www.antipatterns.com\">AntiPatterns.com</a> Web site for the <a href=\"/wiki/AntiPatterns\" title=\"AntiPatterns\">AntiPatterns</a> book</li>\n" +
                "<li><a rel=\"nofollow\" class=\"external text\" href=\"http://www.personal.psu.edu/cjn6/Personal/Antipatterns-%20Patterns%20of%20Toxic%20Behavior.htm\">Patterns of Toxic Behavior</a></li>\n" +
                "<li><a rel=\"nofollow\" class=\"external text\" href=\"https://docs.google.com/document/d/160BsSq4J46Ds3KZVnX2zMnxc_oZMk-nrnI9srrdk6Ro/pub\">C Pointer Antipattern</a></li>\n" +
                "<li><a rel=\"nofollow\" class=\"external text\" href=\"https://leanpub.com/email-antipatterns/\">Email Anti-Patterns</a> book</li>\n" +
                "<li><a rel=\"nofollow\" class=\"external text\" href=\"http://publicsphereproject.org/anti-patterns/\">Patterns of Social Domination</a></li></ul>\n" +
                "<!-- \n" +
                "NewPP limit report\n" +
                "Parsed by mw1331\n" +
                "Cached time: 20200306193701\n" +
                "Cache expiry: 2592000\n" +
                "Dynamic content: false\n" +
                "Complications: [vary‐revision‐sha1]\n" +
                "CPU time usage: 0.320 seconds\n" +
                "Real time usage: 0.454 seconds\n" +
                "Preprocessor visited node count: 1417/1000000\n" +
                "Post‐expand include size: 25540/2097152 bytes\n" +
                "Template argument size: 2427/2097152 bytes\n" +
                "Highest expansion depth: 15/40\n" +
                "Expensive parser function count: 1/500\n" +
                "Unstrip recursion depth: 1/20\n" +
                "Unstrip post‐expand size: 37766/5000000 bytes\n" +
                "Number of Wikibase entities loaded: 1/400\n" +
                "Lua time usage: 0.153/10.000 seconds\n" +
                "Lua memory usage: 4.68 MB/50 MB\n" +
                "-->\n" +
                "<!--\n" +
                "Transclusion expansion time report (%,ms,calls,template)\n" +
                "100.00%  406.285      1 -total\n" +
                " 52.16%  211.909      1 Template:Reflist\n" +
                " 28.73%  116.709      5 Template:Cite_book\n" +
                " 17.22%   69.973      1 Template:Commonscat\n" +
                " 14.13%   57.404      1 Template:Short_description\n" +
                " 12.56%   51.019      2 Template:ISBN\n" +
                " 11.56%   46.974      1 Template:Original_research_inline\n" +
                " 10.16%   41.280      1 Template:Pagetype\n" +
                "  9.54%   38.773      1 Template:Fix\n" +
                "  5.79%   23.533      1 Template:Category_handler\n" +
                "-->\n" +
                "\n" +
                "<!-- Saved in parser cache with key enwiki:pcache:idhash:233956-0!canonical and timestamp 20200306193700 and revision id 936925327\n" +
                " -->\n" +
                "</div><noscript><img src=\"//en.wikipedia.org/wiki/Special:CentralAutoLogin/start?type=1x1\" alt=\"\" title=\"\" width=\"1\" height=\"1\" style=\"border: none; position: absolute;\" /></noscript></div>\n" +
                "\t\t<div class=\"printfooter\">Retrieved from \"<a dir=\"ltr\" href=\"https://en.wikipedia.org/w/index.php?title=Anti-pattern&amp;oldid=936925327\">https://en.wikipedia.org/w/index.php?title=Anti-pattern&amp;oldid=936925327</a>\"</div>\n" +
                "\t\t<div id=\"catlinks\" class=\"catlinks\" data-mw=\"interface\"><div id=\"mw-normal-catlinks\" class=\"mw-normal-catlinks\"><a href=\"/wiki/Help:Category\" title=\"Help:Category\">Categories</a>: <ul><li><a href=\"/wiki/Category:Anti-patterns\" title=\"Category:Anti-patterns\">Anti-patterns</a></li><li><a href=\"/wiki/Category:Software_architecture\" title=\"Category:Software architecture\">Software architecture</a></li><li><a href=\"/wiki/Category:Design\" title=\"Category:Design\">Design</a></li><li><a href=\"/wiki/Category:Industrial_and_organizational_psychology\" title=\"Category:Industrial and organizational psychology\">Industrial and organizational psychology</a></li><li><a href=\"/wiki/Category:Organizational_behavior\" title=\"Category:Organizational behavior\">Organizational behavior</a></li><li><a href=\"/wiki/Category:Anti-social_behaviour\" title=\"Category:Anti-social behaviour\">Anti-social behaviour</a></li><li><a href=\"/wiki/Category:Workplace\" title=\"Category:Workplace\">Workplace</a></li></ul></div><div id=\"mw-hidden-catlinks\" class=\"mw-hidden-catlinks mw-hidden-cats-hidden\">Hidden categories: <ul><li><a href=\"/wiki/Category:Articles_with_short_description\" title=\"Category:Articles with short description\">Articles with short description</a></li><li><a href=\"/wiki/Category:All_articles_that_may_contain_original_research\" title=\"Category:All articles that may contain original research\">All articles that may contain original research</a></li><li><a href=\"/wiki/Category:Articles_that_may_contain_original_research_from_March_2018\" title=\"Category:Articles that may contain original research from March 2018\">Articles that may contain original research from March 2018</a></li><li><a href=\"/wiki/Category:Commons_category_link_is_on_Wikidata\" title=\"Category:Commons category link is on Wikidata\">Commons category link is on Wikidata</a></li></ul></div></div>\n" +
                "\t\t<div class=\"visualClear\"></div>\n" +
                "\t\t\n" +
                "\t</div>";

        WordTrie dico = WordTrie.builder()
                .addKeyword("Analysis paralysis")
                .addKeyword("Bicycle shed")
                .addKeyword("Stovepipe or Silos")
                .addKeyword("Vendor lock-in")
                .addKeyword("Smoke and mirrors")
                .addKeyword("Copy and paste programming")
                .addKeyword("Golden hammer")
                .build();

        assertEquals(13, dico.parseText(text).size());
    }

    @Test
    public void parseText_basic_html() throws Exception {
        WordTrie dico = WordTrie.builder().ignoreCase()
                .addKeyword("Analysis paralysis")
                .build();

        String[] expectedKeyWord = {"Analysis paralysis"};
        assertArrayEquals(expectedKeyWord, dico.parseText("<b>Analysis paralysis</b>").toArray());
        assertArrayEquals(expectedKeyWord, dico.parseText("<b> Analysis</b> <i>paralysis</i>").toArray());
        assertTrue(dico.parseText("<p class=\" Analysis paralysis \">Hello</p>").isEmpty());
        assertTrue(dico.parseText("<!-- Analysis paralysis --> Hello").isEmpty());
    }
}
%% LaTeX2e class for student theses
%% sdthesis.cls
%% 
%% Karlsruhe Institute of Technology
%% Institute for Program Structures and Data Organization
%% Chair for Software Design and Quality (SDQ)
%%
%% Dr.-Ing. Erik Burger
%% burger@kit.edu
%%
%% Version 1.1, 2014-11-21
%%
%% SVN-Informationen:
%% ------------------
%% $LastChangedBy: burger $
%% $LastChangedRevision: 3448 $
%% $LastChangedDate: 2014-11-21 10:55:23 +0100 (Fri, 21 Nov 2014) $
%% $HeadURL: https://svnserver.informatik.kit.edu/i43/svn/lehre/Abschlussarbeiten-Vorlage/sdqthesis.cls $

\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{sdqthesis}[2014/10/21 v1.0 Student thesis class]
\DeclareOption{ngerman}{\PassOptionsToPackage{\CurrentOption}{babel}}
% German is also needed in English thesis for the abstract
\DeclareOption{english}{\PassOptionsToPackage{main=\CurrentOption,ngerman}{babel}}
\ProcessOptions\relax

%% ---------------------------------------------------------------
%% | Based on KOMAscript by Markus Kohm http://www.komascript.de |
%% ---------------------------------------------------------------
\LoadClass[12pt,headsepline,a4paper,titlepage=true,DIV=15,BCOR=20mm]{scrreprt}

%% -----------------------
%% | Headers and footers |
%% -----------------------
\RequirePackage[automark]{scrpage2}

\clubpenalty=10000 %prevent orphans
\widowpenalty=10000 %prvent widows

%% --------------
%% | Typography |
%% --------------

% T1 font encoding
\RequirePackage[T1]{fontenc}
\RequirePackage[utf8]{inputenc}

% serif type: Linux Libertine
\RequirePackage{libertine}
% Linux Libertine in math mode
\RequirePackage[libertine]{newtxmath}

% grotesque type: Adobe Source Sans Pro
\RequirePackage[scaled=.92]{sourcesanspro}

% monospace type: Bera Mono
\RequirePackage[scaled=.78]{beramono}

% semi-bold type (for subsections and paragraphs)
\newcommand*{\sbdefault}{sb}
\DeclareRobustCommand{\sbseries}{%
  \not@math@alphabet\sbseries\relax
  \fontseries\sbdefault\selectfont}

\DeclareTextFontCommand{\textsb}{\sbseries}

\addtokomafont{subsection}{\sbseries}
\addtokomafont{subsubsection}{\sbseries}
\addtokomafont{paragraph}{\sbseries}

% microtype for nicer typography
\RequirePackage[protrusion=true,expansion=true]{microtype}

% commands for code and model keywords in text
\newcommand{\code}[1]{\texttt{\hyphenchar\font45\relax #1}}
\newcommand{\model}[1]{\textsf{#1}}

% nicer spacing for enumerations
\RequirePackage{enumitem}


%% --------------
%% | Title page |
%% --------------

% Logos
\newcommand{\thegrouplogo}{sdqlogo}
\newcommand{\grouplogo}[1]{\renewcommand{\thegrouplogo}{#1}}
\newcommand{\printgrouplogo}{\includegraphics[height=1.8cm]{logos/\thegrouplogo}}
\newcommand{\nogrouplogo}{\renewcommand{\printgrouplogo}{}}

\titlehead{%
% KIT logo german/english
\iflanguage{ngerman}
{\includegraphics[height=1.8cm]{logos/kitlogo_de_cmyk}}
{\includegraphics[height=1.8cm]{logos/kitlogo_en_cmyk}}
\hfill
% SDQ logo
\printgrouplogo
\ThisCenterWallPaper{1}{title-background.pdf}
}
		
%
\setkomafont{title}{\huge\sffamily\bfseries}
\setkomafont{subtitle}{\normalfont\large}

\uppertitleback{Karlsruher Institut für Technologie\\ Fakultät für Informatik\\ Postfach 6980\\ 76128 Karlsruhe}

%% variables for title page
\newcommand{\theinstitute}{\iflanguage{english}
    {Institute for Program Structures and Data Organization (IPD)}
    {Institut für Programmstrukturen und Datenorganisation (IPD)}}
\newcommand{\thethesistype}{}
\newcommand{\thereviewerone}{}
\newcommand{\thereviewertwo}{}
\newcommand{\theadvisorone}{}
\newcommand{\theadvisortwo}{}
\newcommand{\theeditstart}{}
\newcommand{\theeditend}{}

%% formatting commands for titlepage
\newcommand{\thesistype}[1]{\subtitle{\vskip2em #1}%
\renewcommand{\thethesistype}{#1}}
\newcommand{\myinstitute}[1]{\renewcommand{\theinstitute}{#1}}
\newcommand{\reviewerone}[1]{\renewcommand{\thereviewerone}{#1}}
\newcommand{\reviewertwo}[1]{\renewcommand{\thereviewertwo}{#1}}
\newcommand{\advisorone}[1]{\renewcommand{\theadvisorone}{#1}}
\newcommand{\advisortwo}[1]{\renewcommand{\theadvisortwo}{#1}}

\newcommand{\editingtime}[2]{%
\renewcommand{\theeditstart}{#1}%
\renewcommand{\theeditend}{#2}%
%% do not show the date
\date{}
}

\newcommand{\settitle}{%
\publishers{%
\large
\iflanguage{english}{Department of Informatics}%
{an der Fakultät für Informatik}\\
\theinstitute\\[2em]
}
}

%% -----------------------------
%% | Abstract/Acknowledgements |
%% -----------------------------

%\newcommand{\abstract}[1][\abstractname]{\chapter*{#1}}
%\newcommand{\Abstract}[1][\abstractname]{\chapter*{#1}\addcontentsline{toc}{chapter}{#1}} 

\def\ackname{Acknowledgments}
\def\abstractname{Abstract}
\def\switcht@deutsch{\svlanginfo
	\def\ackname{Danksagung}
	\def\abstractname{Kurzfassung}
}
\def\switcht@english{\svlanginfo
	\def\ackname{Acknowledgements}
	\def\abstractname{Abstract}
}

%% In English theses, an additional German Abstract is needed.
\newcommand{\includeabstract}{
\iflanguage{english}{
% include English and German abstracts
\include{sections/abstract_en}
\begin{otherlanguage}{ngerman}
\include{sections/abstract_de}
\end{otherlanguage}
}{
% include only German abstract
\include{sections/abstract_de}
}
}

%% ------------
%% | Packages |
%% ------------

% draft mode
\RequirePackage{ifdraft}
\RequirePackage{ifthen}

% enumerate subsubsections
\setcounter{secnumdepth}{3}

% wallpaper for title page (KIT-Frame)
\RequirePackage{wallpaper}

% subimport of files
\RequirePackage{import}

% languages
\RequirePackage{babel}

% hyphenation for compound words
\RequirePackage[shortcuts]{extdash}

% nice tables
\RequirePackage{booktabs}
\RequirePackage{longtable}
\RequirePackage{array}

% show graphics in draft mode
\RequirePackage{graphicx}
\setkeys{Gin}{draft=false}

% % Mathematics package
% \RequirePackage{amsmath}
% \let\openbox\relax
% \RequirePackage{amsthm}
% \RequirePackage{amssymb}
% \RequirePackage{mathtools}

% appendix
\RequirePackage[toc,title,header]{appendix}
\noappendicestocpagenum

% todo-notes
\RequirePackage[obeyDraft]{todonotes}
\presetkeys{todonotes}{inline}{}

% PDF specific packages
\RequirePackage[hyphens]{url}
\RequirePackage[breaklinks,colorlinks=false]{hyperref}
\newcommand\setpdf{\hypersetup{%
    pdftitle={\thethesistype},%
    pdfsubject={\@title},%
    pdfauthor={\@author},%
    pdfborder={0 0 0},%
    %linkcolor=kit-blue100,%
    %citecolor=kit-green100,%
    %urlcolor=kit-red100
    }%
    \let\theauthor\@author
}

%% automatic naming of sections with autoref
\def\sectionautorefname{Section}
\def\subsectionautorefname{Subsection}
\def\subsubsectionautorefname{Subsection}
\def\paragraphautorefname{Paragraph}
# 在线考试系统

## 介绍
<p style="font-size: 18px">本系统分为学生端和教师端，学生端实现了考试功能和查询成绩功能，教师端实现了添加题目、发布考试
、结束考试、主观题评分和查询成绩功能</p>
<p style="font-size: 18px">该系统使用java gui实现</p>

## 项目包介绍
> MySql包:存放java对数据库进行操作的函数<br/>
> Student:存放学生界面类<br/>
> Teacher：存放教师界面类

## 项目功能介绍
### 学生端
<ul>
    <li>
        考试功能
        <p>学生在考试前，需要选择考卷，每份考卷只能作答一次，学生提交考卷后，不能再进入该考卷的作答页面</p>
    </li>
    <li>
        查询成绩
        <p>教师结束考试后，学生可以查询到自己的成绩，学生可以看到试卷总分和成绩，成绩分为客观题成绩和主观题成绩，客观题成绩在学生提交考卷后由系统进行评分，主观题成绩需要教师进行评分，主观题成绩默认为0分</p>
    </li>
</ul>

### 教师端
<ul>
    <li>
        录入题目功能
        <p>教师选择自己任教的科目，可以录入的题目类型为：单选题、多选题、主观题</p>
        <p>教师需要输入题目、分值，如果题目类型为单选或多选，则需要教师输入选项和答案</p>
    </li>
    <li>
        发布考试功能
        <p>教师选择自己任教的科目，选择需要考试的班级</p>
        <p>本功能采取随机组卷的方式，需要教师指定试卷的题目数</p>
    </li>
    <li>
        结束考试功能
        <p>教师选择一场正在进行的考试，结束该考试，结束考试后学生不能再进入该考试的作答页面</p>
    </li>
    <li>
        主观题评分功能
        <p>教师选择一张试卷里一道主观题进行评分</p>
        <p>教师评分过程中，可以直接提交评分结果（关闭窗口即为提交评分结果）</p>
    </li>
    <li>
        查看成绩功能
        <p>教师选择试卷，查看学生成绩</p>
    </li>
</ul>

## 数据库介绍
> 读者需要使用MySql数据库读取onlineexams.sql

### class 班级表
|     字段     | 含义属性 |   类型    |   长度   | 备注  |
|:----------:|:----:|:-------:|:------:|:---:|
|  ClassId   | 班级号  |  char   |   7    | 主键  |
| ClassName  | 班级名  | varchar |   20   ||

### student 学生表
|    字段     | 含义属性 |   类型    |  长度   |        备注        |
|:---------:|:----:|:-------:|:-----:|:----------------:|
|    Sno    |  学号  |  char   |  12   |        主键        |
|Sname|  姓名  | varchar |20|     外键 Class     |
|Password|密码|char|20|       登录密码       |
|ClassId|班级号|char|7| 外键 Class ClassId |

### teacher 教师表
|     字段     | 含义属性 |   类型    |   长度   | 备注  |
|:----------:|:----:|:-------:|:------:|:---:|
|Tno| 教师编号 |char|12|主键|
|Tname| 教师名  |varchar|4||
|Password|  密码  |char|20|登录密码|

### subject 科目表
|   字段    | 含义属性 |   类型    |   长度   | 备注  |
|:-------:|:----:|:-------:|:------:|:---:|
|SubjectId|科目号|char|5|主键|
| SubjectName|科目名|varchar|20|| 

### taught 教授表
|     字段     | 含义属性 |   类型    |   长度   |         备注          |
|:----------:|:----:|:-------:|:------:|:-------------------:|
|Tno|教师编号|char|12|   外键 teacher Tno    |
|ClassId|班级号|char|7| 主键 外键 class ClassId |
|SubjectId|科目号|char|5|     外键 subject SubejctId|

### questionbank 题库
|     字段     | 含义属性 |   类型    | 长度  |           备注            |
|:----------:|:----:|:-------:|:---:|:-----------------------:|
|SubjectId|科目号|char|  5  | 主键 外键 subject SubjectId |
|ProblemId|题目号|char|  3  |           主键            |
|ProblemType|题目类型|int|     |   0代表单选 1代表多选 2代表主观题    |
|Problem|题目|text| |                         |
|grade|分值|decimal|3 .1|        3位，保留一位小数        |

### objectivequestions 客观题
|     字段     | 含义属性 |   类型    | 长度  |           备注            |
|:----------:|:----:|:-------:|:---:|:-----------------------:|
| SubjectId  | 科目号  |char|5|主键 外键 Subject SubjectId|
| ProblemId  | 题目号  |char|3|主键 外键 qustionbank ProblemId|
|   Answer   |  答案  |char|4||
| Selection1 | 选项1  |text| | |
| Selection2 | 选项2  |text| | |
| Selection3 | 选项3  |text| | |
| Selection4 | 选项4  |text| | |

### page 试卷表
|     字段     | 含义属性 |   类型    | 长度  |             备注              |
|:----------:|:----:|:-------:|:---:|:---------------------------:|
|PaegId|试卷号|char|5|             主键              |
|SubjectId|科目号|char|5|   主键 外键 subject SubjectId   |
|ProblemId|题目号|char|3| 主键 外键 qustionbank ProblemId |
|ProblemType|题目类型|int| |     0代表单选 1代表多选 2代表主观题      |

### groupvolumes 组卷表
|     字段     | 含义属性 |   类型    | 长度  |             备注          |
|:----------:|:----:|:-------:|:---:|:-----------------------:|
|PageId|试卷号|char|5|      主键 外键 page PageId  |
|Tno|教师编号|char|12|      主键 外键 teacher Tno  |
|ClassId|班级号|char|7|     主键 外键 class ClassId |
|SubjectId|科目号|char|5|   主键 外键 subject SubjectId |
|state|试卷状态|int| |  0代表考试状态 1代表结束状态        |

### studnetanswer 学生答案表
|     字段     | 含义属性 |  类型  | 长度  |              备注              |
|:----------:|:----:|:----:|:---:|:----------------------------:|
|Sno|  学号  | char | 12  |      主键 外键 studnet Sno       |
|PageId| 试卷号  | char |  5  |      主键 外键 page pageId       |
|ProblemId| 题目号  | char |  3  | 主键 外键 questionbank ProblemId |
|Answer|  答案  | text |     |           学生主观题答案            |

### score 成绩表
|     字段     | 含义属性 |   类型    | 长度  |        备注         |
|:----------:|:----:|:-------:|:---:|:-----------------:|
|Sno|学号|char|12| 主键 外键 student Sno |
|PageId|试卷号|char|5| 主键 外键 page PageId |
|PageScore|试卷总分|decimal| 4 .1|  试卷总成绩 4位，保留一位小数  |
|ObjectiveScore|客观题分数|decimal|4 .1|  客观题得分 4位，保留一位小数  |
|SubjectiveScore|主观题分数|decimal|4 .1|主观题得分 4位，保留一位小数|
